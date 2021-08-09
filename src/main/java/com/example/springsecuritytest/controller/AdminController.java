package com.example.springsecuritytest.controller;


import com.example.springsecuritytest.dto.MemberDto;
import com.example.springsecuritytest.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.SQLException;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController { // admin

    private final MemberService memberService;

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

    @GetMapping("/info")
    public String showMemberInfo(@RequestParam(required = false) Long id, Model model) throws SQLException {
        MemberDto memberDto = memberService.findById(id);
        model.addAttribute("member", memberDto);

        return "myInfo";
    }

}
