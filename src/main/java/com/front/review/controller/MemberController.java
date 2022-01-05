package com.front.review.controller;

import com.front.review.dto.MemberDto;
import com.front.review.enumclass.Role;
import com.front.review.service.MemberService;
import lombok.AllArgsConstructor;
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
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/home")
    public String memberHome(HttpSession session) {
        if (session.getAttribute("categoryList") != null) {
//            session.setAttribute("categoryList", categoryService.getAllCategories());
        } else {
            session.removeAttribute("categoryList");
        }
        return "home/member-home";
    }

    // 내정보 페이지
    @GetMapping("/info")
    public String showMyInfo(HttpSession session, Model model) {
        if (session.getAttribute("memberInfo") != null) {
            model.addAttribute("memberInfo", session.getAttribute("memberInfo"));
            return "my-Info";
        } else {
            return "redirect:/login";
        }
    }

    @PostMapping("/info")
    public String updateMyInfo(HttpSession session, MemberDto memberInfo) {
        memberService.updateMemberInfo(session, memberInfo);
        return "redirect:/member/home";
    }
}
