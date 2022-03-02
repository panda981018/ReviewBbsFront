package com.front.review.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.front.review.dto.CategoryDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryRequestHandler { // back 단의 api를 호출하는 기능

    private final ObjectMapper objectMapper;
    private String baseUrl = "http://localhost:9000";

    public List<CategoryDto> sendCategoryListRequest() {
        RestTemplate restTemplate = new RestTemplate();
        String url = baseUrl + "/api/category/get/all";

        return restTemplate.exchange(url,
                HttpMethod.GET,
                null,
                List.class).getBody();
    }
}
