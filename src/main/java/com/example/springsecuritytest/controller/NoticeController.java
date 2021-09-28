package com.example.springsecuritytest.controller;

import com.example.springsecuritytest.domain.entity.NoticeEntity;
import com.example.springsecuritytest.dto.MemberDto;
import com.example.springsecuritytest.dto.NoticeDto;
import com.example.springsecuritytest.service.ImageService;
import com.example.springsecuritytest.service.NoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/notice")
public class NoticeController {

    private final NoticeService noticeService;

    @GetMapping("/")
    public String showNoticePage(@PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
                                 Model model) {
        Page<NoticeEntity> noticeEntities = noticeService.getAllNotices(pageable);
        Page<NoticeDto> noticeList = noticeEntities.map(NoticeEntity::toDto);
        model.addAttribute("noticeList", noticeList);
        return "notice/notice";
    }

    @GetMapping("/view")
    public String viewNotice(@RequestParam String id, Model model) {
        NoticeDto notice = noticeService.getNotice(Long.parseLong(id));
        model.addAttribute("notice", notice);
        return "notice/viewnotice";
    }

    @GetMapping("/write")
    public String showWritePage(Model model) {
        model.addAttribute("noticeDto", new NoticeDto());
        return "notice/writenotice";
    }

    @PostMapping("/write")
    public String createNotice(HttpSession session, NoticeDto noticeDto) { // 매개변수 : NoticeDto noticeDto
        noticeService.createNotice(session, noticeDto);

        return "redirect:/notice/";
    }

    @GetMapping("/update")
    public String showUpdatePage(@RequestParam String id, Model model) {
        NoticeDto notice = noticeService.getNotice(Long.parseLong(id));
        System.out.println(notice);
        model.addAttribute("noticeDto", notice);
        return "notice/editnotice";
    }

    @PostMapping("/update")
    public String updateNotice(NoticeDto noticeDto, HttpSession member) {
        MemberDto memberDto = (MemberDto) member.getAttribute("memberInfo");
        noticeDto.setWriterRole(memberDto.getRole());
        noticeService.updateNotice(noticeDto);

        return "redirect:/notice/";
    }

    @ResponseBody
    @PostMapping("/ajax/update/views") // 조회수 업데이트 기능
    public void updateViews(@RequestBody HashMap<String, String> noticeId) {
        noticeService.updateViews(Long.parseLong(noticeId.get("id")));
    }

    @ResponseBody
    @DeleteMapping("/ajax/delete")
    public void deleteNotice(@RequestBody HashMap<String, Object> noticeData) {
        String id = noticeData.get("id").toString();
        List<String> urls = (List<String>) noticeData.get("urls");
        noticeService.deleteNotice(Long.valueOf(id), urls);
    }
}
