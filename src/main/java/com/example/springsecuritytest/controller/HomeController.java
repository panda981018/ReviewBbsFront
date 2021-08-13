package com.example.springsecuritytest.controller;

import com.example.springsecuritytest.dto.MemberDto;
import com.example.springsecuritytest.enumclass.Role;
import com.example.springsecuritytest.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.sql.SQLException;
import java.util.HashMap;

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

    @PostMapping("/check/email")
    @ResponseBody
    public HashMap<String, Boolean> checkEmail(@RequestBody Object usernameObj) {
        HashMap<String, String> obj = (HashMap<String, String>) usernameObj;
        return memberService.checkEmail(obj.get("username"));
    }

    @PostMapping("/check/nickname")
    @ResponseBody
    public HashMap<String, Boolean> checkNickname(@RequestBody Object nicknameObj) throws SQLException {
        HashMap<String, String> obj = (HashMap<String, String>) nicknameObj;
        return memberService.checkNickname(obj.get("id"), obj.get("nickname"), obj.get("view"));
    }

    // 회원가입 페이지
    @GetMapping("/signup")
    public String showSignUpPage(Model model) {
        model.addAttribute("memberDto", new MemberDto());

        return "signup";
    }

    // 회원가입 처리
    @PostMapping("/signup")
    public String signUp(MemberDto memberDto, Errors errors) {

        if (errors.hasErrors()) {
            return "signup";
        } else {
            if (memberDto.getRole().equals("ADMIN")) {
                memberDto.setRole(Role.ADMIN.getValue());
                memberDto.setGender("");
                memberDto.setYear(null);
                memberDto.setMonth(null);
                memberDto.setDay(null);
            } else if (memberDto.getRole().equals("MEMBER")) {
                memberDto.setRole(Role.MEMBER.getValue());
            }

            memberService.signUp(memberDto);
        }
        return "redirect:/";
    }

}
