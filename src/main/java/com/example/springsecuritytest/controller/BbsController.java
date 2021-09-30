package com.example.springsecuritytest.controller;

import com.example.springsecuritytest.dto.BbsDto;
import com.example.springsecuritytest.dto.CategoryDto;
import com.example.springsecuritytest.dto.MemberDto;
import com.example.springsecuritytest.dto.ReplyDto;
import com.example.springsecuritytest.service.BbsService;
import com.example.springsecuritytest.service.CategoryService;
import com.example.springsecuritytest.service.ReplyService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;

@Controller
@AllArgsConstructor
@RequestMapping("/post")
public class BbsController {

    private final BbsService bbsService;
    private final CategoryService categoryService;
    private final ReplyService replyService;

    @GetMapping
    public String defaultCategory(HttpSession session) {
        if (categoryService.getAllCategories().isEmpty()) {
            return "post/nocategory";
        } else {
            List<CategoryDto> categoryDtoList = categoryService.getAllCategories();
            session.setAttribute("categoryList", categoryDtoList);

            return "redirect:/post/bbs?category=" + categoryDtoList.get(0).getId();
        }
    }

    @GetMapping("/bbs")
    public String getAllBbs(@RequestParam(required = false) String category, Model model) {
        // view에서 category에 대한 정보를 표시하기 위해
        // (session.categoryList는 index 0 부터 시작. DB의 카테고리는 1부터 시작. 따라서 -1)
        model.addAttribute("categoryIndex", Long.parseLong(category) - 1);
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
        BbsDto bbs = null;
        Long viewCategoryId = 0L;
        List<ReplyDto> replies;

        HashMap<String, Object> dataMap = bbsService.getBbs(Long.parseLong(id));

        if (dataMap.get("bbsDto") instanceof BbsDto) {
            bbs = (BbsDto) dataMap.get("bbsDto");
            viewCategoryId = bbs.getCategoryId() - 1;
        }

        if (dataMap.get("replies") instanceof List<?>) {
            replies = replyService.getReplies(Long.parseLong(id));
            if (!replies.isEmpty()) {
                model.addAttribute("replyList", replies);
            }
        }
        model.addAttribute("bbs", bbs);
        model.addAttribute("categoryId", viewCategoryId);

        return "post/viewBbs";
    }

    @GetMapping("/bbs/update") // 수정하는 페이지
    public String showBbsList(@RequestParam(required = false) String id, Model model) {

        BbsDto bbs = null;

        HashMap<String, Object> dataMap = bbsService.getBbs(Long.parseLong(id));

        if (dataMap.get("bbsDto") instanceof BbsDto) {
            bbs = (BbsDto) dataMap.get("bbsDto");
        }
        model.addAttribute("bbs", bbs);
        return "post/editBbs";
    }

    @PostMapping("/bbs/update") // 수정 클릭
    public String updateBbs(BbsDto bbsDto, HttpSession session) {
        MemberDto member = (MemberDto) session.getAttribute("memberInfo");
        bbsService.updateBbs(bbsDto, member);
        return "redirect:/post/bbs?category=" + bbsDto.getCategoryId();
    }
}
