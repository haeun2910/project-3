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

    @GetMapping("user-update")
    public String update() {
        return "/users/update.html";
    }

    @GetMapping("/signup")
    public String signup() {
        return "/users/sign-up-form.html";
    }

    @GetMapping("shop-list")
    public String shopList() {
        return "/shops/shop-list.html";
    }

    @GetMapping("home-page")
    public String homePage() {
        return "/shops/home-page.html";
    }
    @GetMapping("signin-home-page")
    public String signinHomePage() {
        return "/shops/signin-home-page.html";
    }

    @GetMapping("get-shop/{id}")
    public String getShop() {
        return "/shops/shop-profile.html";
    }

    @GetMapping("business-application")
    public String businessApplication() {
        return "/users/business-application.html";
    }
}
