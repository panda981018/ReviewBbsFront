package com.front.review.controller;

import com.front.review.enumclass.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class HomeController {
    // 메인 페이지
    @GetMapping("/")
    public String index(Authentication auth) {
        if (auth == null) {
            return "home/index";
        } else if (auth.getAuthorities().contains(new SimpleGrantedAuthority(Role.MEMBER.getValue()))) {
            return "redirect:/member/home";
        } else {
            return "redirect:/admin/home";
        }
    }

    // 로그인 페이지
    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }
}
