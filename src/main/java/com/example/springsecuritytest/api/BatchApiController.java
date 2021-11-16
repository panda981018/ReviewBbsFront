package com.example.springsecuritytest.api;

import com.example.springsecuritytest.service.BatchService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@Controller
@RequestMapping("/api/")
@RequiredArgsConstructor
public class BatchApiController {

    private final BatchService batchService;

    @GetMapping("/getData")
    @ResponseBody
    public void getDateAndCount(@RequestParam String category) {
        batchService.getBatchDate(category.toUpperCase());
//        HashMap<String, Object> dataSet = new HashMap<>();
    }
}
