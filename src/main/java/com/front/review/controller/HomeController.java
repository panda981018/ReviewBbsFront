package com.front.review.controller;

import com.front.review.dto.MemberDto;
import com.front.review.enumclass.Role;
import com.front.review.service.BbsService;
import com.front.review.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final MemberService memberService;
    private final BbsService bbsService;

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

    @PostMapping("/signup")
    public String postSignup(MemberDto memberDto) {
        memberDto.setRole(memberDto.getRole().equals("ADMIN") ? Role.ADMIN.getValue() : Role.MEMBER.getValue());
        Long userId = memberService.signUp(memberDto);

        if (userId != null)
            return "redirect:/";
        else
            return "error";
    }

    @PostMapping("/api/check/email")
    public ResponseEntity<Boolean> checkOverlapEmail(@RequestBody HashMap<String, String> usernameObj) {
        return memberService.checkEmail(usernameObj);
    }

    @PostMapping("/api/check/nickname")
    public ResponseEntity<Boolean> checkOverlapNickname(@RequestBody HashMap<String, String> nicknameObj) {
        return memberService.checkNickname(nicknameObj);
    }

    @GetMapping("/home/bbs")
    public ResponseEntity<List> getMainPosts(@RequestParam int perPage) {
        return bbsService.getHomeBbsDtoList(perPage);
    }
}
