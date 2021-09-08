package com.example.springsecuritytest.controller;

import com.example.springsecuritytest.dto.BbsDto;
import com.example.springsecuritytest.dto.MemberDto;
import com.example.springsecuritytest.service.BbsService;
import lombok.AllArgsConstructor;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Controller
@AllArgsConstructor
@RequestMapping("/post")
public class BbsController {

    private final BbsService bbsService;

    @GetMapping
    public String noCategory() {
        return "post/nocategory";
    }

    @GetMapping("/bbs")
    public String showCategoryBbs(@RequestParam(required = false) String category, Model model) {
        // bbs 테이블로 가서 categoryId가 같은 것을 가져온다.
        if (category != null) {
            List<BbsDto> bbsList = bbsService.getBbsList(Long.parseLong(category));
            // view에서 category에 대한 정보를 표시하기 위해
            // (session.categoryList는 index 0 부터 시작. DB의 카테고리는 1부터 시작. 따라서 -1)
            model.addAttribute("categoryIndex", Long.parseLong(category) - 1);
            model.addAttribute("bbsList", bbsList);
        }
        return "post/post";
    }

    @GetMapping("/bbs/write") // /post/bbs/write
    public String showWritePage(@RequestParam String category, Model model) {
        model.addAttribute("bbs", new BbsDto());
        model.addAttribute("categoryId", category);
        return "post/writeBbs";
    }

    @PostMapping("/bbs/write")
    public String createBbs(BbsDto bbsDto, HttpSession session) {
        bbsService.saveBbs(bbsDto, (MemberDto) session.getAttribute("memberInfo"));

        return "redirect:/post/bbs?category=" + bbsDto.getCategoryId();
    }

    @GetMapping("/bbs/view") // 게시글 보기
    public String viewBbs(@RequestParam(required = false) String id, Model model) {
        BbsDto bbs = bbsService.getBbs(Long.parseLong(id));
        Long viewCategoryId = bbs.getCategoryId()-1;

        model.addAttribute("bbs", bbs);
        model.addAttribute("categoryId", viewCategoryId);

        return "post/viewBbs";
    }

    @GetMapping("/bbs/update") // 수정하는 페이지
    public String showBbs(@RequestParam(required = false) String id, Model model) {
        BbsDto bbs = bbsService.getBbs(Long.parseLong(id));
        model.addAttribute("bbs", bbs);
        return "post/editBbs";
    }

    @PostMapping("/bbs/update") // 수정 클릭
    public String updateBbs(BbsDto bbsDto, HttpSession session) {
        MemberDto member = (MemberDto) session.getAttribute("memberInfo");
        bbsService.updateBbs(bbsDto, member);
        return "redirect:/post/bbs?category=" + bbsDto.getCategoryId();
    }

    @ResponseBody
    @PostMapping("/bbs/update/views") // 조회수 업데이트 기능
    public void updateViews(@RequestBody HashMap<String, String> bbsIdObj) {
        bbsService.updateViews(Long.parseLong(bbsIdObj.get("id")));
    }

    @ResponseBody
    @DeleteMapping("/bbs/delete")
    public void deleteBbs(@RequestBody HashMap<String, Object> data) throws JSONException {
        Double dId = (Double) data.get("id");
        if (data.containsKey("urls")) {
            ArrayList<String> urls = (ArrayList<String>) data.get("urls");
            bbsService.deleteBbs(dId.longValue(), urls);
        } else {
            bbsService.deleteBbs(dId.longValue(), null);
        }
    }

    @ResponseBody
    @PostMapping("/bbs/uploadImg")
    public HashMap<String, String> uploadSummernoteImage(@RequestParam("file") List<MultipartFile> multipartFile) {
        return bbsService.uploadImage(multipartFile);
    }
}
