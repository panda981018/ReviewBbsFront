package com.example.springsecuritytest.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class NoticeController {

    @GetMapping("/notice/")
    public String showNoticePage() {

        return "notice";
    }
}
