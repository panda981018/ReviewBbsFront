package com.example.springsecuritytest.api;

import com.example.springsecuritytest.dto.MemberDto;
import com.example.springsecuritytest.service.BbsService;
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
@RequestMapping("/api/heart")
public class HeartApiController {

    private final HeartService heartService;
    private final BbsService bbsService;

    @ResponseBody
    @PostMapping("/like")
    public HashMap<String, Object> likeBbs(@RequestBody HashMap<String, String> likeObj, HttpSession session) {
        String bid = likeObj.get("bbsId");
        String type = likeObj.get("type");
        MemberDto memberDto = (MemberDto) session.getAttribute("memberInfo");

        int updatedLikeCnt;

        if (type.equals("like")) { // like일 경우
            heartService.plusHeartCount(Long.parseLong(bid), memberDto.getId());
        } else { // cancel일 경우
            heartService.minusHeartCount(Long.parseLong(bid), memberDto.getId());
        }
        updatedLikeCnt = bbsService.updateLikeCount(Long.parseLong(bid), memberDto.getId(), type);


        HashMap<String, Object> mapData = new HashMap<>();
        if (updatedLikeCnt == -1) { // 게시물이 존재하지 않음.
            mapData.put("resultCode", -1); // likeCnt 업데이트 오류
        } else { // 게시물이 존재함.
            mapData.put("resultCode", 0); // likeCnt 업데이트 성공
        }
        mapData.put("likeCnt", updatedLikeCnt);

        return mapData;
    }
}