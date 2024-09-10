package com.example.project_3.controller;

import com.example.project_3.UserDto;
import com.example.project_3.entity.User;
import com.example.project_3.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("users")
@RequiredArgsConstructor
public class UserController {
    private final UserService service;


    @GetMapping("/my-profile")
    public String getMyProfile() {
        return "my-profile";


    }

    @PostMapping("/create")
    public UserDto createUser(
            @RequestBody UserDto userDto
    ) {
        return service.createUser(userDto);
    }

    @PutMapping("/update")
    public UserDto updateProfile(
            @RequestBody UserDto userDto

    ){

        Long updateId = service.getUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName()).getId();
        return service.updateProfile(updateId, userDto);
    }
    @PutMapping("/image")
    public UserDto updateImg(
            MultipartFile image
    ){
        Long updateId = service.getUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName()).getId();
        return service.updateImg(updateId, image);
    }
    @DeleteMapping("/delete")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(){
        Long deleteId = service.getUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName()).getId();
        service.delete(deleteId);
    }
    @PostMapping("apply-business")
    public String applyForBusiness() {
        Long updateId = service.getUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName()).getId();
        service.applyForBusiness(updateId);
        return "apply successful";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("business-application")
    public List<User> getBusinessApplications() {
        List<User> businessApplications = service.getBusinessApplications();
        return businessApplications;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("approve-business/{userId}")
    public String approveBusiness(@PathVariable Long userId) {
        service.approveBusinessApplication(userId);
        return "Approved";

    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("reject-business/**")
    public String rejectBusiness(@PathVariable Long userId) {
        service.rejectBusinessApplication(userId);
        return "Rejected";
    }

}
