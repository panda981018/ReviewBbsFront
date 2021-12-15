package com.front.review.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
@RequestMapping("/map")
public class MapController {
    @GetMapping("/")
    public String index(Principal authentication) {
        if (authentication == null) {
            return "redirect:/login";
        } else
            return "map/map";
    }
}
