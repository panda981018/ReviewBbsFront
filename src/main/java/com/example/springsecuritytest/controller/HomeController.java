package com.example.springsecuritytest.controller;

import com.example.springsecuritytest.dto.MemberDto;
import com.example.springsecuritytest.enumclass.Role;
import com.example.springsecuritytest.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class HomeController { // anonymous

    private final MemberService memberService;

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

        return "sign-up";
    }

    // 회원가입 처리
    @PostMapping("/signup")
    public String signUp(MemberDto memberDto) {

        memberDto.setRole(memberDto.getRole().equals("ADMIN") ? Role.ADMIN.getValue() : Role.MEMBER.getValue());
        Long newId = memberService.signUp(memberDto);
        if (newId != null)
            return "redirect:/";
        else
            return "error";
    }

}
