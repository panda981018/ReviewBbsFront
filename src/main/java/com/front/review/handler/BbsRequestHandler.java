package com.front.review.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.front.review.dto.MemberDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.codehaus.groovy.util.HashCodeHelper;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class BbsRequestHandler { // back 단의 api를 호출하는 기능

    private final ObjectMapper objectMapper;
    private String baseUrl = "http://localhost:9000";

    public ResponseEntity<List> sendHomeBbsDtoListRequest(int perPage) {
        RestTemplate restTemplate = new RestTemplate();
        String url = baseUrl + "/api/bbs/get/home";
        HttpHeaders httpHeaders = new HttpHeaders();

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("perPage", String.valueOf(perPage));

        UriComponents uriComponents = UriComponentsBuilder.fromHttpUrl(url)
                .queryParams(map)
                .build();

        HttpEntity<HttpHeaders> entity = new HttpEntity<>(httpHeaders);
        return restTemplate.exchange(uriComponents.toUriString(),
                HttpMethod.GET,
                entity,
                List.class);
    }

    // update views
    public void updateBbsViewsRequest(HashMap<String, String> bbsObj) {
        RestTemplate restTemplate = new RestTemplate();
        String url = baseUrl + "/api/bbs/update/views";
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<HashMap<String, String>> entity = new HttpEntity<>(bbsObj, httpHeaders);
        ResponseEntity<Long> response = restTemplate.postForEntity(url, entity, Long.class);
        log.info("UPDATE BBS VIEWS STATUS CODE = {}", response.getStatusCode());
    }

    // get a bbs
    public HashMap<String, Object> viewBbs(Long bid, Long memberId) {
        RestTemplate restTemplate = new RestTemplate();
        String url = baseUrl + "/api/bbs/view";
        HttpHeaders httpHeaders = new HttpHeaders();

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("bbsId", String.valueOf(bid));
        map.add("memberId", String.valueOf(memberId));

        UriComponents uriComponents = UriComponentsBuilder.fromHttpUrl(url)
                .queryParams(map)
                .build();

        HashMap<String, Object> response = restTemplate.exchange(uriComponents.toUriString(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<HashMap<String, Object>>() {}).getBody();

        return response;
    }
}
