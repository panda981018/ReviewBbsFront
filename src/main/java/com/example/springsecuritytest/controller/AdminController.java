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
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController { // admin

    private final MemberService memberService;
    private final CategoryService categoryService;

    @GetMapping("/home")
    public String adminHome() {
        return "home/adminHome";
    }

    @GetMapping("/manage")
    @ResponseBody
    public HashMap<String, Object> adminPage(@RequestParam(required = false) int perPage,
                                             @RequestParam(required = false) int page,
                                             @RequestParam(required = false) String sort) throws Exception {
        PageRequest pageRequest;
        if (sort == null) {
            pageRequest = PageRequest.of(page - 1, perPage, Sort.by(Sort.Direction.ASC, "id"));
        } else {
            pageRequest
                    = PageRequest.of(page - 1, perPage, Sort.by(Sort.Direction.ASC, sort));
        }

        HashMap<String, Object> dataObj = memberService.getMemberPagination(pageRequest);

        List<MemberDto> contents = (List<MemberDto>) dataObj.get("memberDtoList");
        long memberCount = (long) dataObj.get("totalCount");

        /* 클라이언트에게 보낼 때 지켜야할 데이터 형식
        {
          "result": true,
          "data": {
            "contents": [],
            "pagination": {
              "page": 1,
              "totalCount": 100
            }
          }
        }
         */
        HashMap<String, Object> responseMap = new HashMap<>();
        HashMap<String, Object> dataMap = new HashMap<>();
        HashMap<String, Object> paginationMap = new HashMap<>();

        paginationMap.put("page", page);
        paginationMap.put("totalCount", memberCount);
        dataMap.put("contents", contents);
        dataMap.put("pagination", paginationMap);

        if (contents.size() == 0) {
            responseMap.put("result", false);
        } else {
            responseMap.put("result", true);
        }

        responseMap.put("data", dataMap);

        return responseMap;
    }

//    @GetMapping("/manage")
//    public String adminPage(@PageableDefault(size = 5, sort = "id", direction = Sort.Direction.ASC) Pageable pageable, Model model) {
//
//        Page<MemberEntity> members = memberService.findAllMembers(pageable);
//        Page<MemberDto> memberList = members.map(MemberEntity::toDto);
//        model.addAttribute("memberList", memberList);
//
//        return "admin/admin_member";
//    }

    @GetMapping("/manage/member")
    public String showMemberPage() {
        return "admin/admin_member";
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

    @GetMapping("/chart")
    public String showCategoryChart() {
        return "admin/admin_statics";
    }

}