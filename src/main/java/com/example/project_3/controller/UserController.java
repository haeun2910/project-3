package com.example.project_3.controller;

import com.example.project_3.UserDto;
import com.example.project_3.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
        log.info(service.getUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName()).getId().toString());
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

}
