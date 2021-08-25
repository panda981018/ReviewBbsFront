package com.example.springsecuritytest.controller;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

@Controller
public class ErrorController implements org.springframework.boot.web.servlet.error.ErrorController {

    private final String DEFAULT_ERROR_PATH = "/error";

    @PostMapping("/login/error")
    public String loginError(HttpServletRequest request, Model model) {
        String errorMessage = (String) request.getAttribute("errorMessage");
        model.addAttribute("errorMessage", errorMessage);

        return "login";
    }

    @RequestMapping("/error")
    public String errorHandler(HttpServletRequest request, Model model) {

        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        HttpStatus httpStatus = HttpStatus.valueOf(Integer.valueOf(status.toString()));
        model.addAttribute("errorCode", status.toString());
        model.addAttribute("errorMessage", httpStatus.getReasonPhrase());

        return DEFAULT_ERROR_PATH;
    }

    @RequestMapping(value = "/access-denied", method = RequestMethod.GET)
    public String accessDenied(Model model) {
        model.addAttribute("errorCode", "403");
        model.addAttribute("errorMessage", "접근 권한이 없습니다.\n다른 계정으로 접근하십시오.");

        return DEFAULT_ERROR_PATH;
    }
}
