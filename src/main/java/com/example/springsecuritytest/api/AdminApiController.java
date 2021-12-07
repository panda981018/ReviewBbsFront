package com.example.springsecuritytest.api;

import com.example.springsecuritytest.dto.MemberDto;
import com.example.springsecuritytest.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/")
@RequiredArgsConstructor
public class AdminApiController {

    private final MemberService memberService;

    @GetMapping("/manage")
    public HashMap<String, Object> adminPage(@RequestParam(required = false) int perPage,
                                             @RequestParam(required = false) int page,
                                             @RequestParam(required = false) String sort) throws Exception {
        PageRequest pageRequest;
        if (sort == null) {
            pageRequest = PageRequest.of(page - 1, perPage, Sort.by(Sort.Direction.ASC, "id"));
        } else {
            pageRequest
                    = PageRequest.of(page - 1, perPage, Sort.by(Sort.Direction.ASC, sort));
        }

        HashMap<String, Object> dataObj = memberService.getMemberPagination(pageRequest);

        List<MemberDto> contents = (List<MemberDto>) dataObj.get("memberDtoList");
        long memberCount = (long) dataObj.get("totalCount");

        HashMap<String, Object> responseMap = new HashMap<>();
        HashMap<String, Object> dataMap = new HashMap<>();
        HashMap<String, Object> paginationMap = new HashMap<>();

        paginationMap.put("page", page);
        paginationMap.put("totalCount", memberCount);
        dataMap.put("contents", contents);
        dataMap.put("pagination", paginationMap);

        if (contents.size() == 0) {
            responseMap.put("result", false);
        } else {
            responseMap.put("result", true);
        }

        responseMap.put("data", dataMap);

        return responseMap;
    }
}
