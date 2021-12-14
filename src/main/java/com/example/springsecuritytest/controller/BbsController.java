package com.example.springsecuritytest.controller;

import com.example.springsecuritytest.dto.*;
import com.example.springsecuritytest.service.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;

import static com.example.springsecuritytest.conf.AppConfig.getClientIp;

@Controller
@AllArgsConstructor
@RequestMapping("/post")
public class BbsController {

    private final BbsService bbsService;
    private final CategoryService categoryService;
    private final ReplyService replyService;
    private final HeartService heartService;
    private final FavoriteService favoriteService;

    @GetMapping
    public String defaultCategory(HttpSession session) {
        if (categoryService.getAllCategories().isEmpty()) {
            return "post/no-category";
        } else {
            List<CategoryDto> categoryDtoList = categoryService.getAllCategories();
            session.setAttribute("categoryList", categoryDtoList);

            return "redirect:/post/bbs?category=" + categoryDtoList.get(0).getId();
        }
    }

    @GetMapping("/bbs")
    public String getAllBbs(@RequestParam(required = false) String category, Principal authentication, Model model) {
        if (authentication == null) {
            return "redirect:/login";
        } else {
            // view에서 category에 대한 정보를 표시하기 위해
            // (session.categoryList는 index 0 부터 시작. DB의 카테고리는 1부터 시작. 따라서 -1)
            model.addAttribute("categoryId", Long.parseLong(category) - 1);
            return "post/post";
        }
    }

    @GetMapping("/bbs/write") // /post/bbs/write
    public String showWritePage(@RequestParam String category, Model model) {
        model.addAttribute("bbs", new BbsDto());
        model.addAttribute("categoryId", category);
        return "post/write-bbs";
    }

    @PostMapping("/bbs/write")
    public String createBbs(BbsDto bbsDto, HttpServletRequest request) throws Exception {
        HttpSession session = request.getSession();
        bbsDto.setIpAddr(getClientIp(request));
        bbsService.saveBbs(bbsDto, (MemberDto) session.getAttribute("memberInfo"));

        return "redirect:/post/bbs?category=" + bbsDto.getCategoryId();
    }

    @GetMapping("/bbs/view") // 게시글 보기
    public String viewBbs(@RequestParam(required = false) String id, HttpSession session, Model model) throws Exception {
        BbsDto bbs = null;
        Long viewCategoryId = 0L;
        List<ReplyDto> replies;

        HashMap<String, Object> dataMap = bbsService.getBbs(Long.parseLong(id));
        MemberDto memberDto = (MemberDto) session.getAttribute("memberInfo");
        HeartDto heartDto = heartService.findHeartObject(Long.parseLong(id), memberDto.getId());

        if (dataMap.get("bbsDto") instanceof BbsDto) {
            bbs = (BbsDto) dataMap.get("bbsDto");
            viewCategoryId = bbs.getCategoryId() - 1;
        }

        assert bbs != null;
        boolean favDto = favoriteService.findFavObject(bbs.getLatitude(), bbs.getLongitude(), memberDto.getId());

        if (dataMap.get("replies") instanceof List<?>) {
            replies = replyService.getReplies(Long.parseLong(id));
            if (!replies.isEmpty()) {
                model.addAttribute("replyList", replies);
            }
        }
        model.addAttribute("bbs", bbs);
        model.addAttribute("categoryId", viewCategoryId);
        model.addAttribute("heartObj", heartDto);
        model.addAttribute("favObj", favDto);

        return "post/view-bbs";
    }

    @GetMapping("/bbs/update") // 수정하는 페이지
    public String editBbs(@RequestParam(required = false) String id, Model model) throws Exception {

        BbsDto bbs = null;

        HashMap<String, Object> dataMap = bbsService.getBbs(Long.parseLong(id));

        if (dataMap.get("bbsDto") instanceof BbsDto) {
            bbs = (BbsDto) dataMap.get("bbsDto");
        }
        model.addAttribute("bbs", bbs);
        return "post/edit-bbs";
    }

    @PostMapping("/bbs/update") // 수정 클릭
    public String updateBbs(BbsDto bbsDto, HttpServletRequest request) throws Exception {
        HttpSession session = request.getSession();
        MemberDto member = (MemberDto) session.getAttribute("memberInfo");
        bbsDto.setIpAddr(getClientIp(request));

        bbsService.updateBbs(bbsDto, member);
        return "redirect:/post/bbs?category=" + bbsDto.getCategoryId();
    }
}
