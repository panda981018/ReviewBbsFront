package com.example.springsecuritytest.api;

import com.example.springsecuritytest.dto.CategoryDto;
import com.example.springsecuritytest.service.BatchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Slf4j
@Controller
@RequestMapping("/api/")
@RequiredArgsConstructor
public class BatchApiController {

    private final BatchService batchService;

    @GetMapping("/getData")
    @ResponseBody
    public HashMap<String, List<Object>> getDateAndCount(@RequestParam int year, @RequestParam int month,
                                                         HttpServletRequest request) {
        List<CategoryDto> categoryList = (List<CategoryDto>) request.getSession().getAttribute("categoryList");
        HashMap<String, List<Object>> batchData = new HashMap<>();

        for(CategoryDto category : categoryList) {
            batchData.put(category.getName(), batchService.getBatchDate(category.getName(), year, month));
        }

        return batchData;
    }
}