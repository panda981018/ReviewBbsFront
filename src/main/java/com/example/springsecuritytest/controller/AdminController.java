package com.example.springsecuritytest.controller;

import com.example.springsecuritytest.domain.entity.MemberEntity;
import com.example.springsecuritytest.dto.CategoryDto;
import com.example.springsecuritytest.dto.MemberDto;
import com.example.springsecuritytest.service.CategoryService;
import com.example.springsecuritytest.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.sql.SQLException;
import java.util.HashMap;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController { // admin

    private final MemberService memberService;
    private final CategoryService categoryService;

    @GetMapping("/home")
    public String adminHome(HttpSession session) throws SQLException {

        return "home/adminHome";
    }

    @GetMapping("/manage")
    public String adminPage(@PageableDefault(size = 5, sort = "id", direction = Sort.Direction.ASC) Pageable pageable, Model model) {

        Page<MemberEntity> members = memberService.findAllMembers(pageable);
        Page<MemberDto> memberList = members.map(MemberEntity::toDto);
        model.addAttribute("memberList", memberList);

        return "admin/admin_member";
    }

    @ResponseBody
    @GetMapping("/manage/sort")
    public HashMap<String, Page<MemberDto>> adminMemberPaging(@RequestParam String sort) {
        Pageable pageable = PageRequest.of(0, 5, Sort.Direction.ASC, sort);
        Page<MemberEntity> members = memberService.findAllMembers(pageable);
        Page<MemberDto> memberList = members.map(MemberEntity::toDto);

        HashMap<String, Page<MemberDto>> memberInfo = new HashMap<>();
        memberInfo.put("rs", memberList);

        System.out.println(memberInfo);

        return memberInfo;
    }

    @GetMapping("/info")
    public String showMemberInfo(@RequestParam(required = false) String id, Model model) throws SQLException {
        MemberDto memberDto = memberService.findById(Long.parseLong(id));
        model.addAttribute("memberInfo", memberDto);
        return "myInfo";
    }

    @GetMapping("/category")
    public String showCategoryPage(Model model) {
        model.addAttribute("category", new CategoryDto());

        return "admin/admin_category";
    }

    @PostMapping("/category")
    public String createCategory(CategoryDto categoryDto) {
        categoryService.createCategory(categoryDto);

        return "admin/admin_category";
    }

}