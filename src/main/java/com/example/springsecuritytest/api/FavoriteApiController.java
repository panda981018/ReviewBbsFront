package com.example.springsecuritytest.api;

import com.example.springsecuritytest.dto.MemberDto;
import com.example.springsecuritytest.service.BbsService;
import com.example.springsecuritytest.service.FavoriteService;
import com.example.springsecuritytest.service.HeartService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.HashMap;

@Controller
@AllArgsConstructor
@RequestMapping("/api/fav")
public class FavoriteApiController {

    private final FavoriteService favoriteService;
    private final BbsService bbsService;

    @ResponseBody
    @PostMapping("/map")
    public HashMap<String, String> addOrCancelFav(@RequestBody HashMap<String, String> favObj, HttpSession session) {
        String bid = favObj.get("bbsId"); // 게시물 id
        String type = favObj.get("type"); // 유형 (add/cancel)
        String placeName = favObj.get("placeName"); // 장소 이름
        double lat = Double.parseDouble(favObj.get("lat")); // 위도
        double lng = Double.parseDouble(favObj.get("lng")); // 경도
        MemberDto memberDto = (MemberDto) session.getAttribute("memberInfo"); // 로그인한 계정

        String result = "";

        if (type.equals("add")) { // add 경우
            result = favoriteService.saveLocation(memberDto.getId(), lat, lng, placeName);
        } else { // cancel 경우
            result = favoriteService.cancelLocation(lat, lng, memberDto.getId());
        }

        HashMap<String, String> mapData = new HashMap<>();
        mapData.put("status", result);

        return mapData;
    }
}