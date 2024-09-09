package com.example.project_3.controller;

import com.example.project_3.UserDto;
import com.example.project_3.entity.User;
import com.example.project_3.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("users")
@RequiredArgsConstructor
public class UserController {
    private final UserService service;
    @PostMapping("/create")
    public UserDto createUser(
            @RequestBody UserDto userDto
    ) {
        return service.createUser(userDto);
    }

}
