package com.example.springsecuritytest.controller;

import com.example.springsecuritytest.dto.CategoryDto;
import com.example.springsecuritytest.dto.MemberDto;
import com.example.springsecuritytest.service.CategoryService;
import com.example.springsecuritytest.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController { // admin

    private final MemberService memberService;
    private final CategoryService categoryService;

    @GetMapping("/home")
    public String adminHome() {
        return "home/admin-home";
    }

    // member 페이지 보여주는 함수
    @GetMapping("/manage/member")
    public String showMemberPage() {
        return "admin/admin-member";
    }

    @GetMapping("/info")
    public String showMemberInfo(@RequestParam(required = false) String id, Model model) throws SQLException {
        MemberDto memberDto = memberService.findById(Long.parseLong(id));
        model.addAttribute("memberInfo", memberDto);
        return "my-Info";
    }

    @GetMapping("/category")
    public String showCategoryPage(Model model) {
        model.addAttribute("category", new CategoryDto());

        return "admin/admin-category";
    }

    @PostMapping("/category")
    public String createCategory(CategoryDto categoryDto) {
        categoryService.createCategory(categoryDto);

        return "admin/admin-category";
    }

    @GetMapping("/chart")
    public String showCategoryChart() {
        return "admin/admin-statics";
    }

}