package com.example.springsecuritytest.controller;

import com.example.springsecuritytest.domain.entity.MemberEntity;
import com.example.springsecuritytest.dto.CategoryDto;
import com.example.springsecuritytest.dto.MemberDto;
import com.example.springsecuritytest.service.CategoryService;
import com.example.springsecuritytest.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController { // admin

    private final MemberService memberService;
    private final CategoryService categoryService;

    @GetMapping("/home")
    public String adminHome() {
        return "home/adminHome";
    }

    // member 페이지 보여주는 함수
    @GetMapping("/manage/member")
    public String showMemberPage() {
        return "admin/admin_member";
    }

    @GetMapping("/info")
    public String showMemberInfo(@RequestParam(required = false) String id, Model model) throws SQLException {
        MemberDto memberDto = memberService.findById(Long.parseLong(id));
        model.addAttribute("memberInfo", memberDto);
        return "myInfo";
    }

    @GetMapping("/category")
    public String showCategoryPage(Model model) {
        model.addAttribute("category", new CategoryDto());

        return "admin/admin_category";
    }

    @PostMapping("/category")
    public String createCategory(CategoryDto categoryDto) {
        categoryService.createCategory(categoryDto);

        return "admin/admin_category";
    }

    @GetMapping("/chart")
    public String showCategoryChart() {
        return "admin/admin_statics";
    }

}