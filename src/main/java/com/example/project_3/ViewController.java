package com.example.project_3;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
@RequestMapping("views")
public class ViewController {
    @GetMapping("test")
    public String test() {
        return "test";
    }
    @GetMapping("/login")
    public String login() {
        return "/users/login-form.html";
    }
    @GetMapping("my-profile")
    public String myProfile() {
        return "/users/my-profile.html";
    }
}
