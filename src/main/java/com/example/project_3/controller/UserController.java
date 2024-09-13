package com.example.project_3.controller;

import com.example.project_3.ShopDetails;
import com.example.project_3.UserDto;
import com.example.project_3.entity.User;
import com.example.project_3.service.ShopService;
import com.example.project_3.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("users")
@RequiredArgsConstructor
public class UserController {
    private final UserService service;
    private final ShopService shopService;
    private final UserService userService;


    @PostMapping("/create")
    public ResponseEntity<UserDto> createUser(@RequestBody UserDto userDto) {
        try {
            UserDto createdUser = service.createUser(userDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
        } catch (ResponseStatusException e) {
            // Handle known exceptions
            return ResponseEntity.status(e.getStatusCode()).body(null);
        } catch (Exception e) {
            // Handle unexpected exceptions
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/profile")
    public ResponseEntity<UserDto> getCurrentUserProfile() {
        UserDto userProfile = service.getCurrentUserProfile();
        return ResponseEntity.ok(userProfile);
    }

    @PutMapping("/update-profile")
    public UserDto updateProfile(
            @RequestBody UserDto userDto

    ){

        Long updateId = service.getUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName()).getId();
        return service.updateProfile(updateId, userDto);
    }
    @PostMapping("/profile-image")
    public UserDto updateImg(
            MultipartFile image
    ){
        Long updateId = service.getUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName()).getId();
        return service.updateImg(updateId, image);
    }
    @DeleteMapping("/delete")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Long deleteId = service.getUserByUsername(username).getId();
        service.delete(deleteId);
    }
    @PreAuthorize("hasRole('ROLE_USER')")
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
    @PostMapping("reject-business/{userId}")
    public String rejectBusiness(@PathVariable Long userId) {
        service.rejectBusinessApplication(userId);
        return "Rejected";
    }



}
