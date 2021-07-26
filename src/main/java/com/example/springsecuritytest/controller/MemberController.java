package com.example.springsecuritytest.controller;

import com.example.springsecuritytest.dto.MemberDto;
import com.example.springsecuritytest.dto.SignUpForm;
import com.example.springsecuritytest.service.MemberService;
import com.example.springsecuritytest.validate.SignUpFormValidator;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.sql.SQLException;

@Controller
@AllArgsConstructor
@RequestMapping("/member")
public class MemberController { // member

    private final Logger log = LoggerFactory.getLogger(MemberController.class);

    @Autowired
    private MemberService memberService;
    @Autowired
    private SignUpFormValidator signUpFormValidator;

    @InitBinder("signUpForm")
    public void initBinder(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(signUpFormValidator);
    }

    @GetMapping("/home")
    public String memberHome() {
        return "home/memberHome";
    }

    // 회원가입 페이지
    @GetMapping("/signup")
    public String showSignUpPage(Model model) {
        model.addAttribute("signUpForm", new SignUpForm());

        return "signup";
    }

    // 회원가입 처리
    @PostMapping("/signup")
    public String signUp(@Valid SignUpForm signUpForm, Errors errors, RedirectAttributes attributes) {
        if (errors.hasErrors()) {
            return "signup";
        } else {
            memberService.signUp(signUpForm);
            return "home/index";
        }
    }

    // 로그인 페이지
    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }

    // 내정보 페이지
    @GetMapping("/info")
    public String showMyInfo(Authentication auth, Model model) { // 세션에서 유지되고 있는 Authentication 객체를 가져옴.
        MemberDto member = memberService.findByUsername(auth.getName());
        model.addAttribute("member", member);
        return "myinfo";
    }

    // 내정보 수정하기 클릭했을 때
    @PostMapping("/info/update")
    public String postMyInfo(Authentication auth, MemberDto memberDto) throws SQLException {

        MemberDto beforeMem = memberService.findByUsername(auth.getName());
        if (beforeMem.getPassword().equals(memberDto.getPassword())) {
            memberDto.setPassword(null);
        } else {
            memberDto.setPassword(memberDto.getPassword());
        }

        memberService.updateMember(memberDto);

        return "memberupdate";
    }


}
