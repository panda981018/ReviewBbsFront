package com.example.springsecuritytest.controller;

import com.example.springsecuritytest.domain.entity.MemberEntity;
import com.example.springsecuritytest.dto.MemberDto;
import com.example.springsecuritytest.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.HashMap;

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
    public String adminPage(@PageableDefault(size = 5, sort = "id", direction = Sort.Direction.ASC) Pageable pageable,
                            Model model) {
        Page<MemberEntity> members = memberService.findAllMembers(pageable);
        Page<MemberDto> memberList = members.map(member -> member.toDto());

        model.addAttribute("memberList", memberList);

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