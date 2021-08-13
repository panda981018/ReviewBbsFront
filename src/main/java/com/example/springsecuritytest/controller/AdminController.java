package com.example.springsecuritytest.controller;


import com.example.springsecuritytest.dto.MemberDto;
import com.example.springsecuritytest.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public String adminPage(Authentication authentication, Model model) {
        List<MemberDto> members = memberService.findAllMembers();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        HashMap<String, Object> map = new HashMap<>();
        map.put("members", members);
        map.put("adminId", userDetails.getUsername());
        model.addAllAttributes(map);

        return "admin/manage";
    }

    @PostMapping("/check/admin")
    @ResponseBody
    public HashMap<String, Boolean> checkPassword(@RequestBody HashMap<String, String> password) {

        return memberService.checkPassword(password.get("adminId"), password.get("password"));
    }

    @GetMapping("/info")
    public String showMemberInfo(@RequestParam(required = false) Long id, Model model) throws SQLException {
        MemberDto memberDto = memberService.findById(id);
        model.addAttribute("member", memberDto);

        return "myInfo";
    }

}