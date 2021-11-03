package com.example.springsecuritytest.controller;

import com.example.springsecuritytest.dto.MemberDto;
import com.example.springsecuritytest.dto.ReplyDto;
import com.example.springsecuritytest.service.ReplyService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;

import static com.example.springsecuritytest.conf.AppConfig.getClientIp;

@Controller
@AllArgsConstructor
@RequestMapping("/reply")
public class ReplyController {

    private final ReplyService replyService;

    @ResponseBody
    @PostMapping("/ajax/add")
    public HashMap<String, String> createReply(HttpServletRequest request, @RequestBody HashMap<String, String> obj) throws Exception {
        HttpSession session = request.getSession();
        String bbsId = obj.get("bbsId");
        System.out.println(bbsId);
        ReplyDto replyDto = ReplyDto.builder()
                .contents(obj.get("contents"))
                .bbs(Long.parseLong(bbsId))
                .ipAddr(getClientIp(request))
                .build();
        replyService.createReply(session, replyDto);

        HashMap<String, String> map = new HashMap<>();
        map.put("responseCode", "ok");
        return map;
    }

    @ResponseBody
    @DeleteMapping("/ajax/delete")
    public void deleteReply(@RequestBody HashMap<String, String> deleteData) {

        String bbsId = deleteData.get("bbsId");
        String replyId = deleteData.get("replyId");
        replyService.removeReply(Long.parseLong(replyId), Long.parseLong(bbsId));
    }

    @ResponseBody
    @PostMapping("/ajax/update")
    public void updateReply(@RequestBody HashMap<String, String> updateObj, HttpSession session) {
        MemberDto member = (MemberDto) session.getAttribute("memberInfo");
        replyService.updateReply(updateObj.get("contents"), Long.parseLong(updateObj.get("id")), member);
    }
}
