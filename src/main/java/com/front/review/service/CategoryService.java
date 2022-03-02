package com.front.review.service;

import com.front.review.dto.CategoryDto;
import com.front.review.handler.CategoryRequestHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.List;

@Service
@RequiredArgsConstructor
@EnableWebSecurity
public class CategoryService {

    private final CategoryRequestHandler categoryRequestHandler;

    public void getAllCategoryList(HttpSession session) {
        List<CategoryDto> responseEntity = categoryRequestHandler.sendCategoryListRequest();
        if (session.getAttribute("categoryList") == null) {
            session.setAttribute("categoryList", responseEntity);
        }
        System.out.println("stop categoryList session");
    }
}
