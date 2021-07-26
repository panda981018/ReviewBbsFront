package com.example.springsecuritytest.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController { // anonymous

    // 메인 페이지
    @GetMapping("/")
    public String index() { return "home/index";
    }

}
