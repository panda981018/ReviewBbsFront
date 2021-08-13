package com.example.springsecuritytest.controller;

import com.example.springsecuritytest.dto.MemberDto;
import com.example.springsecuritytest.enumclass.Role;
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

    @GetMapping("/home")
    public String memberHome(HttpSession session) {
        System.out.println(session.getId());
        return "home/memberHome";
    }

    // 내정보 페이지
    @GetMapping("/info")
    public String showMyInfo(Authentication auth, Model model) throws SQLException { // 세션에서 유지되고 있는 Authentication 객체를 가져옴.
        MemberDto member = memberService.findByUsername(auth.getName());
        model.addAttribute("member", member);
        return "myInfo";
    }

    // 내정보에서 update 클릭했을 때
    @PostMapping("/info")
    public String postMyInfo(Authentication auth, MemberDto memberDto){

        memberService.updateMember(memberDto);

        if (auth.getAuthorities().equals(Role.ADMIN.getValue())) {
            return "redirect:/admin/home";
        } else if (auth.getAuthorities().equals(Role.MEMBER.getValue())) {
            return "redirect:/member/home";
        } else {
            return "redirect:/";
        }
    }
}
