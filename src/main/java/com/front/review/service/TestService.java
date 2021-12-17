package com.front.review.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.front.review.dto.TestRequestDto;
import com.front.review.util.MultiValueMapConverter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.nio.charset.StandardCharsets;

@Service
@Slf4j
@RequiredArgsConstructor
public class TestService {
    private final ObjectMapper objectMapper;
    private String baseUrl = "http://localhost:9000";

    public String useRestTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        // 임시 데이터 생성
        TestRequestDto testDto = TestRequestDto.builder()
                .id(1)
                .msg("requestDto")
                .build();
        // url
        String url = baseUrl + "/api/hello2";
        // 헤더 설정
        HttpHeaders httpHeaders = new HttpHeaders();

        // 1. MultiValueMap에 직접 넣어주는 방식
//        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
//        map.add("id", String.valueOf(testDto.getId()));
//        map.add("msg", testDto.getMsg());

        // 2. 만들어둔 abstract class를 통해 MultiValueMap으로 변환
        // package : com.front.review.util.MultiValueMapConverter
        MultiValueMap<String, String> map = MultiValueMapConverter.convert(objectMapper, testDto);

        UriComponents uriComponents = UriComponentsBuilder.fromUriString(url)
                .queryParams(map)
                .build();

        // httpentity에 헤더 및 params 설정
        HttpEntity<HttpHeaders> entity = new HttpEntity<>(httpHeaders);

        //  요청 중 응답 확인
        ResponseEntity<String> responseEntity2 = restTemplate.exchange(uriComponents.toUriString(),
                HttpMethod.GET,
                entity,
                String.class);

        log.info(String.valueOf(responseEntity2.getStatusCode()));
        log.info(responseEntity2.getBody());
        return responseEntity2.getBody();
    }
}
