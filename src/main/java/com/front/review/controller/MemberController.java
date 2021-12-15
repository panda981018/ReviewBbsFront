package com.front.review.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.sql.SQLException;

@Controller
@AllArgsConstructor
@RequestMapping("/member")
public class MemberController {

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
    public String showMyInfo(HttpSession session, Model model) throws SQLException { // 세션에서 유지되고 있는 Authentication 객체를 가져옴.

        model.addAttribute("memberInfo", session.getAttribute("memberInfo"));
        return "my-Info";
    }
}
