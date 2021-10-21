package com.example.springsecuritytest.controller;

import com.example.springsecuritytest.dto.MemberDto;
import com.example.springsecuritytest.service.MapService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Set;

@Controller
@RequiredArgsConstructor
@RequestMapping("/map")
public class MapController { // anonymous

    private final MapService mapService;

    @GetMapping("/")
    public String index() {
        return "map/map";
    }

    // ajax
    @GetMapping("/getMarkers")
    @ResponseBody
    public HashMap<String, Object> sendMarkerInfo(HttpSession session) {
        Set<HashMap<String, Object>> result
                = mapService.getPlaceInfo((MemberDto) session.getAttribute("memberInfo"));
        HashMap<String, Object> hash = new HashMap<>();
        hash.put("response", result);
        return hash;
    }
}
