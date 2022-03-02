package com.front.review.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.front.review.dto.*;
import com.front.review.service.BbsService;
import com.front.review.service.CategoryService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;

@Controller
@AllArgsConstructor
@RequestMapping("/post")
public class BbsController {

    private final BbsService bbsService;
    private final CategoryService categoryService;

    @GetMapping("/")
    public String getAllCategoryList(HttpSession session) { // 카테고리 리스트를 가져오자
        categoryService.getAllCategoryList(session);
        return "redirect:/post/bbs?category=1";
    }
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

    @GetMapping("/bbs/view")
    public String viewBbs(@RequestParam Long id, HttpSession session, Model model) {
        MemberDto memberDto = (MemberDto) session.getAttribute("memberInfo");
        HashMap<String, Object> response = bbsService.viewBbs(id, memberDto.getId());

        ObjectMapper objectMapper = new ObjectMapper();
        BbsDto bbs = objectMapper.convertValue(response.get("bbsDto"), BbsDto.class);

        if (response.get("heartObj") != null) {
            HeartDto heart = objectMapper.convertValue(response.get("heartObj"), HeartDto.class);
            model.addAttribute("heartObj", heart);
        } else {
            model.addAttribute("heartObj", null);
        }

        List<ReplyDto> replies = objectMapper.convertValue(response.get("replies"), new TypeReference<List<ReplyDto>>() {});
        if (replies.size() != 0) {
            model.addAttribute("replies", replies);
        }

        model.addAttribute("bbsDto", bbs);
        model.addAttribute("categoryId", response.get("categoryId"));
        model.addAttribute("favObj", response.get("favObj"));

        return "post/view-bbs";
    }

    @PostMapping("/bbs/update/views")
    public void updateBbsViews(@RequestBody HashMap<String, String> bbsObj) {
        bbsService.updateBbsViews(bbsObj);
    }
}
