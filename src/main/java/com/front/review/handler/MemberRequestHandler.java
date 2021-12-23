package com.front.review.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.front.review.dto.MemberDto;
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

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberRequestHandler { // back 단의 api를 호출하는 기능

    private final ObjectMapper objectMapper;
    private String baseUrl = "http://localhost:9000";

    public MemberDto getMemberDto(String username) {
        RestTemplate restTemplate = new RestTemplate();
        String url = baseUrl + "/api/member/getUser";
        HttpHeaders httpHeaders = new HttpHeaders();

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("username", username);

        UriComponents uriComponents = UriComponentsBuilder.fromHttpUrl(url)
                .queryParams(map)
                .build();

        HttpEntity<HttpHeaders> entity = new HttpEntity<>(httpHeaders);

        ResponseEntity<MemberDto> responseEntity = restTemplate.exchange(uriComponents.toUriString(),
                HttpMethod.GET,
                entity,
                MemberDto.class);

        return responseEntity.getBody();
    }

    public Long signUpRequest(MemberDto memberDto) {
        RestTemplate restTemplate = new RestTemplate();
        String url = baseUrl + "/api/member/signup";
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<MemberDto> entity = new HttpEntity<>(memberDto, httpHeaders);
        ResponseEntity<Long> response = restTemplate.postForEntity(url, entity, Long.class);

        log.info("save SUCCESS");

        return response.getBody();
    }

    public ResponseEntity<Boolean> checkEmail(HashMap<String, String> usernameObj) {
        RestTemplate restTemplate = new RestTemplate();
        String url = baseUrl + "/api/check/email";

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<HashMap<String, String>> entity = new HttpEntity<>(usernameObj, httpHeaders);

        ResponseEntity<Boolean> response = restTemplate.postForEntity(url, entity, Boolean.class);
        log.info("checkEmail status = {}", response.getStatusCode());
        log.info("checkEmail body = {}", response.getBody());

        return response;
    }

    public ResponseEntity<Boolean> checkNickname(HashMap<String, String> nicknameObj) {
        RestTemplate restTemplate = new RestTemplate();
        String url = baseUrl + "/api/check/nickname";

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        ResponseEntity<Boolean> response = restTemplate.postForEntity(url, nicknameObj, Boolean.class);
        log.info("checkNickname status = {}", response.getStatusCode());
        log.info("checkNickname body = {}", response.getBody());

        return response;
    }
}
