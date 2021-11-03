package com.example.springsecuritytest.controller;

import com.example.springsecuritytest.dto.BbsDto;
import com.example.springsecuritytest.dto.FavoriteDto;
import com.example.springsecuritytest.dto.MemberDto;
import com.example.springsecuritytest.service.MapService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;

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
    public HashMap<String, Object> sendMarkerInfo(HttpServletRequest request, @RequestParam int page) {
        HttpSession session = request.getSession();
        PageRequest pageRequest = PageRequest.of(page-1, 5, Sort.by(Sort.Direction.ASC, "id"));

        HashMap<String, Object> result
                = mapService.getPlaceInfo((MemberDto) session.getAttribute("memberInfo"), pageRequest);

//        List<FavoriteDto> dtos = (List<FavoriteDto>) result.get("data");
//        int totalPages = (int) result.get("totalPages");
//        hash.put("data", dtos);
//        hash.put("totalPages", totalPages);
        return result;
    }

    @GetMapping("/bbsList")
    @ResponseBody
    public HashMap<String, Object> sendBbsList(@RequestParam double lat, @RequestParam double lng) {
        List<BbsDto> bbsList = mapService.getBbsList(lat, lng);

        HashMap<String, Object> map = new HashMap<>();
        map.put("result", bbsList);

        return map;
    }
}
