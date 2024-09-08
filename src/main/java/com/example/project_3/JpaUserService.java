package com.example.project_3;

import com.example.project_3.entity.UserEntity;
import com.example.project_3.repo.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Slf4j
@Service
public class JpaUserService implements UserDetailsService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository repository;

    public JpaUserService(
            PasswordEncoder passwordEncoder,
            UserRepository repository
    ) {
        this.passwordEncoder = passwordEncoder;
        this.repository = repository;
        UserEntity alex = new UserEntity();
        alex.setUsername("alex");
        alex.setPassword(passwordEncoder.encode("password"));
        alex.setAuthorities("ROLE_USER,READ");
        repository.save(alex);

        UserEntity brad = new UserEntity();
        brad.setUsername("brad");
        brad.setPassword(passwordEncoder.encode("password"));
        brad.setAuthorities("ROLE_ADMIN,READ,WRITE");
        repository.save(brad);
    }

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
        Optional<UserEntity> optionalUser =
                repository.findByUsername(username);
        if (optionalUser.isEmpty())
            throw new UsernameNotFoundException(username);
        UserEntity user = optionalUser.get();
        String[] authorities = user.getAuthorities().split(",");

        return User.withUsername(username)
                .password(user.getPassword())
                .authorities(authorities)
                .build();
    }

    public void createUser(
            String username,
            String password,
            String passCheck
    ) {
        if (repository.existsByUsername(username) ||
                !password.equals(passCheck))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

        UserEntity newUser = new UserEntity();
        newUser.setUsername(username);
        newUser.setPassword(passwordEncoder.encode(password));
        newUser.setAuthorities("ROLE_USER,READ");
        repository.save(newUser);
    }


}
