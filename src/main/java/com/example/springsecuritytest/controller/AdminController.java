package com.example.springsecuritytest.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController { // admin

    @GetMapping("/home")
    public String adminHome() {
        return "home/adminHome";
    }

    @GetMapping("/admin")
    public String adminPage() {
        return "admin";
    }
}
