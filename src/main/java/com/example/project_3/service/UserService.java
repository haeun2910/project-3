package com.example.project_3.service;


import com.example.project_3.UserDto;
import com.example.project_3.entity.User;
import com.example.project_3.repo.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service

public class UserService implements UserDetailsService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository repository;

    public UserService(PasswordEncoder passwordEncoder, UserRepository repository){
        this.passwordEncoder = passwordEncoder;
        this.repository = repository;

//        User hanh = new User();
//        hanh.setUsername("haeun");
//        hanh.setPassword(passwordEncoder.encode("123456"));
//        hanh.setNickname("haeun29");
//        hanh.setFullName("이하은");
//        hanh.setAgeGroup(20);
//        hanh.setEmail("hanh291029@naver.com");
//        hanh.setPhone("01067652699");
//        hanh.setAuthorities("ROLE_ADMIN");
//        hanh.setActive(true);
//        this.repository.save(hanh);
//
//        User alex = new User();
//        alex.setUsername("alex");
//        alex.setPassword(passwordEncoder.encode("password"));
//        hanh.setAuthorities("ROLE_DEFAULT");
//        this.repository.save(alex);
//
//        User chad = new User();
//        chad.setUsername("chad");
//        chad.setPassword(passwordEncoder.encode("password"));
//        chad.setAuthorities("ROLE_USER");
//        chad.setNickname("chad123");
//        chad.setFullName("Chad Tran");
//        chad.setAgeGroup(40);
//        chad.setEmail("chad@gmail.com");
//        chad.setPhone("01063452269");
//        chad.setActive(true);
//        this.repository.save(chad);
//
//        User tran = new User();
//        tran.setUsername("tranLam");
//        tran.setPassword(passwordEncoder.encode("password"));
//        tran.setNickname("wuan");
//        tran.setFullName("LAMHUYENTRAN");
//        tran.setAgeGroup(20);
//        tran.setEmail("huyentran@gmail.com");
//        tran.setPhone("01043920027");
//        tran.setAuthorities("ROLE_BUSINESS");
//        tran.setActive(true);
//        this.repository.save(tran);

    }

    // CREATE
    public UserDto createUser(UserDto dto
    ) {
        if (repository.existsByUsername(dto.getUsername()) ||
                !dto.getPassword().equals(dto.getPassCheck()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

        User newUser = new User();
        newUser.setUsername(dto.getUsername());
        newUser.setPassword(passwordEncoder.encode(dto.getPassword()));
        newUser.setAuthorities("ROLE_DEFAULT");
        return UserDto.fromEntity(repository.save(newUser));
    }
    public UserDto getCurrentUserProfile() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<User> user = repository.findByUsername(username);
        if (user.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }

        User currentUser = user.get();
        System.out.println("Authorities: " + currentUser.getAuthorities()); // Log the authorities

        return UserDto.fromEntity(currentUser);
    }
    // read by username
    public UserDto getUserByUsername(String username) {
        Optional<User> user = repository.findByUsername(username);
        if (user.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return UserDto.fromEntity(user.get());
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> optionalUser =
                repository.findByUsername(username);
        if (optionalUser.isEmpty())
            throw new UsernameNotFoundException(username);
        User user = optionalUser.get();
        String[] authorities = user.getAuthorities().split(",");

        return org.springframework.security.core.userdetails.User.withUsername(username)
                .password(user.getPassword())
                .authorities(authorities)
                .build();
    }
    // UPDATE
    public UserDto updateProfile(Long id, UserDto dto){
        Optional<User> optionalUser = repository.findById(id);
        User target = optionalUser.get();
        target.setNickname(dto.getNickname());
        target.setFullName(dto.getFullName());
        target.setEmail(dto.getEmail());
        target.setPhone(dto.getPhone());
        target.setAgeGroup(dto.getAgeGroup());
        if (dto.getNickname() == null ||
                dto.getFullName() == null ||
                dto.getEmail() == null ||
                dto.getPhone() == null ||
                dto.getAgeGroup() == null
        ){
            target.setAuthorities("ROLE_DEFAULT");
        }
        target.setAuthorities("ROLE_USER");
        target.setActive(true);
        return UserDto.fromEntity(repository.save(target));
    }
    // UPDATE profileImg
    public UserDto updateImg(Long id, MultipartFile image){
        Optional<User> optionalUser = repository.findById(id);
        if (optionalUser.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        String profileDir = "media/" + id + "/";
        try {
            Files.createDirectories(Path.of(profileDir));
        }catch (IOException e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        String originalFileName = image.getOriginalFilename();
        String[] fileNameSplit = originalFileName.split("\\.");
        String extension = fileNameSplit[fileNameSplit.length - 1];
        String uploadPath = profileDir + "profile." + extension;
        try{
            image.transferTo(Path.of(uploadPath));
        }catch (IOException e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        String reqPath = "/static/" + id + "/profile." + extension;
        User targetImg = optionalUser.get();
        targetImg.setProfileImgUrl(reqPath);
        return UserDto.fromEntity(repository.save(targetImg));
    }
    public void delete(Long id){

        if (repository.existsById(id))
            repository.deleteById(id);
        else throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    public void applyForBusiness(Long id){
        Optional<User> user = repository.findById(id);

        if (user.isPresent() && !"ROLE_USER".equals(user.get().getAuthorities())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only users with role USER can apply for business");
        }
        user.get().setBusinessApplication(true);
        repository.save(user.get());
    }
    public List<User> getBusinessApplications(){
        checkIfAdmin();
        return repository.findByBusinessApplicationTrueAndAuthorities("ROLE_USER");
    }
    public void approveBusinessApplication(Long id){
        checkIfAdmin();
        Optional<User> user = repository.findById(id);
        if (user.isPresent() && user.get().isBusinessApplication() && "ROLE_USER".equals(user.get().getAuthorities())) {
            user.get().setAuthorities("ROLE_BUSINESS");
            user.get().setBusinessApplication(false);
            repository.save(user.get());
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Cannot approve application for this user");
        }
    }
    public void rejectBusinessApplication(Long id){
        checkIfAdmin();
        Optional<User> user = repository.findById(id);
        if (user.isPresent() && user.get().isBusinessApplication() && "ROLE_USER".equals(user.get().getAuthorities())) {
            user.get().setBusinessApplication(false);
            repository.save(user.get());
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Cannot reject application for this user");
        }
    }
    private void checkIfAdmin() {
        String currentUserRole = SecurityContextHolder.getContext().getAuthentication().getAuthorities().toString();
        if (!currentUserRole.contains("ROLE_ADMIN")) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You do not have admin privileges");
        }
    }

}
