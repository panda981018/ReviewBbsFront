package com.front.review.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

@Controller
@AllArgsConstructor
@RequestMapping("/post")
public class BbsController {

    @GetMapping("/bbs")
    public String getAllBbs(@RequestParam(required = false) String category, Principal authentication, Model model) {
        if (authentication == null) {
            return "redirect:/login";
        } else {
            // view에서 category에 대한 정보를 표시하기 위해
            // (session.categoryList는 index 0 부터 시작. DB의 카테고리는 1부터 시작. 따라서 -1)
            model.addAttribute("categoryId", Long.parseLong(category) - 1);
            return "post/post";
        }
    }
}
