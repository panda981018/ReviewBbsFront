package com.example.springsecuritytest.controller;

import com.example.springsecuritytest.dto.MemberDto;
import com.example.springsecuritytest.service.MemberService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.sql.SQLException;

@Controller
@AllArgsConstructor
@RequestMapping("/member")
public class MemberController { // member

    @Autowired
    private MemberService memberService;

    @GetMapping("/home")
    public String memberHome(HttpSession session) {
        System.out.println(session.getId());
        return "home/memberHome";
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

    // 내정보에서 update 클릭했을 때
    @PostMapping("/info")
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
