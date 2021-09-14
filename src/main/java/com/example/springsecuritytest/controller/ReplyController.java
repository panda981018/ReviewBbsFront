package com.example.springsecuritytest.controller;

import com.example.springsecuritytest.dto.ReplyDto;
import com.example.springsecuritytest.service.ReplyService;
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
@RequestMapping("/reply")
public class ReplyController {

    private final ReplyService replyService;

    @ResponseBody
    @PostMapping("/ajax/add")
    public HashMap<String, String> createReply(HttpSession session, @RequestBody HashMap<String, String> obj) {
        String bbsId = obj.get("bbsId");
        System.out.println(bbsId);
        ReplyDto replyDto = ReplyDto.builder()
                .contents(obj.get("contents"))
                .bbs(Long.parseLong(bbsId))
                .build();
        replyService.createReply(session, replyDto);

        HashMap<String, String> map = new HashMap<>();
        map.put("responseCode", "ok");
        return map;
    }

}
