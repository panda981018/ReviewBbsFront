package com.example.springsecuritytest.controller;


import com.example.springsecuritytest.dto.MemberDto;
import com.example.springsecuritytest.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController { // admin

    @Autowired
    private MemberService memberService;

    @GetMapping("/home")
    public String adminHome() {
        return "home/adminHome";
    }

    @GetMapping("/manage")
    public String adminPage(Model model) {

        List<MemberDto> members = memberService.findAllMembers();
        model.addAttribute("members", members);

        return "admin/manage";
    }
}
