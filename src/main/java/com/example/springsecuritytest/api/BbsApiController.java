package com.example.springsecuritytest.api;

import com.example.springsecuritytest.dto.BbsDto;
import com.example.springsecuritytest.service.BbsService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@Controller
@AllArgsConstructor
@RequestMapping("/api/bbs")
public class BbsApiController {

    private final BbsService bbsService;

    @ResponseBody
    @GetMapping("/get")
    public HashMap<String, List<BbsDto>> getAllBbsList(@RequestParam(required = false) String category) {
        List<BbsDto> bbsList =  bbsService.findAll(Long.parseLong(category));
        HashMap<String, List<BbsDto>> map = new HashMap<>();
        map.put("bbsList", bbsList);
        return map;
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

    @ResponseBody
    @GetMapping("/sort")
    public HashMap<String, Page<BbsDto>> bbsPaging(@RequestParam String sort,
                                                   @RequestParam String category) {
        System.out.println(sort);
        String[] sortStandard = sort.split(",");
        Sort.Direction direction = (sortStandard[1].equals("asc")) ? Sort.Direction.ASC : Sort.Direction.DESC;

        Pageable pageable = PageRequest.of(0, 5, direction, sortStandard[0]);
        Page<BbsDto> bbsList = bbsService.findAllBbs(pageable, Long.parseLong(category));

        HashMap<String, Page<BbsDto>> bbsObj = new HashMap<>();
        bbsObj.put("bbsList", bbsList);

        return bbsObj;
    }
}
