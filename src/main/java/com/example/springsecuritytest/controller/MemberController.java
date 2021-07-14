package com.example.springsecuritytest.controller;

import com.example.springsecuritytest.dto.MemberDto;
import com.example.springsecuritytest.service.MemberService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@AllArgsConstructor
public class MemberController {

    private MemberService memberService;

    // 메인 페이지
    @GetMapping("/")
    public String index() {
        return "index";
    }

    // 회원가입 페이지
    @GetMapping("/user/signup")
    public String dispSignup() {
        return "signup";
    }

    // 회원가입 처리
    @PostMapping("/user/signup")
    public String execSignup(MemberDto memberDto) {
        memberService.joinUser(memberDto);

        return "redirect:/user/login";
    }

    // 로그인 페이지
    @GetMapping("/user/login")
    public String login() {
        return "login/login";
    }

    // 로그인 결과 페이지
    @GetMapping("/user/login/result")
    public String loginResult() {
        return "login/loginSuccess";
    }

    @GetMapping("/user/login/failure")
    public String loginFailure() {
        return "login/loginFailure";
    }

    // 로그아웃 성공하면 오는 페이지
    @GetMapping("/user/logout/result")
    public String logout() {
        return "login/logout";
    }

    @GetMapping
    public String logoutFailure() {
        return "login/logoutFailure";
    }

    // 접근 거부 페이지
    @GetMapping("/user/denied")
    public String denied() {
        return "denied";
    }

    // 내정보 페이지
    @GetMapping("/user/info")
    public String myInfo() {
        return "myinfo";
    }

    // admin 페이지
    @GetMapping("/admin")
    public String admin() {
        return "admin";
    }
}
