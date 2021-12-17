package com.front.review.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.front.review.service.TestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
public class TestController {

    private final TestService testService;

    @GetMapping("/") // 첫 화면을 보여줄 controller
    public String test() {
        return "testview/test-index";
    }

    @GetMapping("/hello") // 응답이 돌아왔을 때 화면을 보여줄 controller
    public String hello(Model model) throws JsonProcessingException {
        model.addAttribute("response", testService.useRestTemplate());
        return "testview/test-response-page";
    }
}
