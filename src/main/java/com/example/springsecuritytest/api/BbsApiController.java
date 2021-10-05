package com.example.springsecuritytest.api;

import com.example.springsecuritytest.dto.BbsDto;
import com.example.springsecuritytest.service.BbsService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;

@Controller
@AllArgsConstructor
@RequestMapping("/api/bbs")
public class BbsApiController {

    private final BbsService bbsService;

    @ResponseBody
    @GetMapping("/get")
    public HashMap<String, Object> getAllBbsList(@RequestParam(required = false) String category,
                                                 @RequestParam(required = false) String column,
                                                 HttpServletRequest request) {
        List<BbsDto> dataObj = bbsService.findAll(Long.parseLong(category), column);
        HashMap<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("data", dataObj);
        return jsonMap;
    }

    @ResponseBody
    @PostMapping("/update/views") // 조회수 업데이트 기능
    public void updateViews(@RequestBody HashMap<String, String> bbsIdObj) {
        bbsService.updateViews(Long.parseLong(bbsIdObj.get("id")));
    }

    @ResponseBody
    @DeleteMapping("/delete")
    public void deleteBbs(@RequestBody HashMap<String, Object> data) {

        if (data.containsKey("urls")) {
            bbsService.deleteBbs(((Integer) data.get("id")).longValue(), (List<String>) data.get("urls"));
        } else {
            bbsService.deleteBbs(((Integer) data.get("id")).longValue(), null);
        }
    }
}
