package com.example.springsecuritytest.controller;

import com.example.springsecuritytest.dto.MemberDto;
import com.example.springsecuritytest.enumclass.Role;
import com.example.springsecuritytest.service.MemberService;
import lombok.AllArgsConstructor;
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

    private final MemberService memberService;

    @GetMapping("/home")
    public String memberHome(HttpSession session, Authentication auth) throws SQLException {

        MemberDto member = memberService.findByUsername(auth.getName());
        if (session.getAttribute("memberInfo") == null) {
            session.setAttribute("memberInfo", member);
        }
        return "home/memberHome";
    }

    // 내정보 페이지
    @GetMapping("/info")
    public String showMyInfo(HttpSession session, Model model) throws SQLException { // 세션에서 유지되고 있는 Authentication 객체를 가져옴.

        model.addAttribute("memberInfo", session.getAttribute("memberInfo"));
        return "myInfo";
    }

    // 내정보에서 update 클릭했을 때
    @PostMapping("/info")
    public String postMyInfo(HttpSession session, MemberDto memberDto){

        memberService.updateMember(memberDto);
        MemberDto oldDto = (MemberDto) session.getAttribute("memberInfo");
        if (oldDto.getRole().equals(Role.ADMIN.getTitle())) {
            return "redirect:/admin/home";
        } else if (oldDto.getRole().equals(Role.MEMBER.getTitle())) {
            return "redirect:/member/home";
        } else {
            return "redirect:/";
        }
    }
}
