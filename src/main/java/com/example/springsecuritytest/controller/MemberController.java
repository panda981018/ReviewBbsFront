package com.example.springsecuritytest.controller;

import com.example.springsecuritytest.dto.MemberDto;
import com.example.springsecuritytest.service.MemberService;
import com.example.springsecuritytest.validate.MyInfoValidator;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import javax.websocket.server.PathParam;
import java.sql.SQLException;
import java.util.HashMap;

@Controller
@AllArgsConstructor
@RequestMapping("/member")
public class MemberController { // member

    private final MemberService memberService;

    private final MyInfoValidator myInfoValidator;

    @InitBinder("memberDto")
    public void initBinder(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(myInfoValidator);
    }


    @GetMapping("/home")
    public String memberHome(HttpSession session) {
        System.out.println(session.getId());
        return "home/memberHome";
    }

    // 내정보 페이지
    @GetMapping("/info")
    public String showMyInfo(Authentication auth, Model model) throws SQLException { // 세션에서 유지되고 있는 Authentication 객체를 가져옴.
        MemberDto member = memberService.findByUsername(auth.getName());
        model.addAttribute("memberDto", member);
        return "myInfo";
    }

    // 내정보에서 update 클릭했을 때
    @PostMapping("/info")
    public String postMyInfo(@Valid MemberDto memberDto, Errors errors){

        if (errors.hasErrors()) {
            return "myInfo";
        }
        memberService.updateMember(memberDto);

        return "redirect:/member/home";
    }
}
