package com.example.springsecuritytest.controller;

import com.example.springsecuritytest.dto.CategoryDto;
import com.example.springsecuritytest.service.PostService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@AllArgsConstructor
@RequestMapping("/post")
public class PostController {

    private final PostService postService;

    @GetMapping("/")
    public String showPostPage(@RequestParam(required = false) String category, Model model) {

        List<CategoryDto> categoryList = postService.getAllCategories();
        model.addAttribute("categoryList", categoryList);
        return "post";
    }

    @GetMapping("/bbs")
    public String showCategoryBbs(@RequestParam(required = false) String category, Model model) {



        return "post";
    }
}
