package com.front.review.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController { // admin

    @GetMapping("/home")
    public String adminHome() {
        return "home/admin-home";
    }

    // member 페이지 보여주는 함수
    @GetMapping("/manage/member")
    public String showMemberPage() {
        return "admin/admin-member";
    }

    @GetMapping("/chart")
    public String showCategoryChart() {
        return "admin/admin-statics";
    }

}