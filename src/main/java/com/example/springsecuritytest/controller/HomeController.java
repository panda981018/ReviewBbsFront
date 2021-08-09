package com.example.springsecuritytest.controller;

import com.example.springsecuritytest.dto.MemberDto;
import com.example.springsecuritytest.service.MemberService;
import com.example.springsecuritytest.service.Role;
import com.example.springsecuritytest.validate.SignUpValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
public class HomeController { // anonymous

    private final MemberService memberService;
    private final SignUpValidator signUpValidator;

    @InitBinder("memberDto")
    public void initBinder(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(signUpValidator);
    }

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
    public String signUp(@Valid MemberDto memberDto, Errors errors) {

        System.out.println("[HomeController] MemberDto:" + memberDto);

        if (errors.hasErrors()) {
            return "signup";
        } else {
            if (memberDto.getRole().equals(Role.ADMIN.getValue())) {
                memberDto.setGender("");
                memberDto.setYear(null);
                memberDto.setMonth(null);
                memberDto.setDay(null);
            }

            memberService.signUp(memberDto);
        }
        return "redirect:/";
    }

}
