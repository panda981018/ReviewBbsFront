package com.example.springsecuritytest.controller;

import com.example.springsecuritytest.dto.SignUpForm;
import com.example.springsecuritytest.service.MemberService;
import com.example.springsecuritytest.service.Role;
import com.example.springsecuritytest.validate.SignUpFormValidator;
import org.springframework.beans.factory.annotation.Autowired;
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
public class HomeController { // anonymous

    @Autowired
    private MemberService memberService;
    @Autowired
    private SignUpFormValidator signUpFormValidator;

    @InitBinder("signUpForm")
    public void initBinder(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(signUpFormValidator);
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

    // 회원가입 페이지
    @GetMapping("/signup")
    public String showSignUpPage(Model model) {
        model.addAttribute("signUpForm", new SignUpForm());

        return "signup";
    }

    // 회원가입 처리
    @PostMapping("/signup")
    public String signUp(@Valid SignUpForm signUpForm, Errors errors) {
        if (errors.hasErrors()) {
            return "signup";
        } else {
            memberService.signUp(signUpForm);
            return "home/index";
        }
    }

}
