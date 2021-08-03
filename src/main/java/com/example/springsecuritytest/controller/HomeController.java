package com.example.springsecuritytest.controller;

import com.example.springsecuritytest.dto.MemberDto;
import com.example.springsecuritytest.service.MemberService;
import com.example.springsecuritytest.service.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class HomeController { // anonymous

    @Autowired
    private MemberService memberService;

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

    // 회원가입 페이지
    @GetMapping("/signup")
    public String showSignUpPage(Model model) {
        model.addAttribute("memberDto", new MemberDto());

        return "signup";
    }

    // 회원가입 처리
    @PostMapping("/signup")
    public String signUp(MemberDto memberDto) {

        if (memberDto.getRole().equals("Admin")) {
            memberDto.setGender("");
            memberDto.setAge(0);
        }

        memberService.signUp(memberDto);
        return "redirect/signupsuccess";
    }

}
