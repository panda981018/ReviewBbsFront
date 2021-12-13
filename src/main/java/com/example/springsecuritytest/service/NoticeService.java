package com.example.springsecuritytest.service;

import com.example.springsecuritytest.domain.notice.NoticeEntity;
import com.example.springsecuritytest.domain.notice.NoticeQueryRepository;
import com.example.springsecuritytest.domain.notice.NoticeRepository;
import com.example.springsecuritytest.dto.MemberDto;
import com.example.springsecuritytest.dto.NoticeDto;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class NoticeService {

    private final ImageService imageService;
    private final NoticeRepository noticeRepository;
    private final NoticeQueryRepository noticeQueryRepository;

    public void createNotice(HttpSession session, NoticeDto noticeDto) {

        LocalDateTime now = LocalDateTime.now();
        String time = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        noticeDto.setCreateDate(time);
        noticeDto.setViews(0);

        MemberDto memberDto = (MemberDto) session.getAttribute("memberInfo");
        String writerRole = memberDto.getRole();

        noticeDto.setWriterRole(writerRole);

        noticeRepository.save(noticeDto.toEntity());
    }

    public NoticeDto getNotice(Long id) { // 공지사항 1개

        Optional<NoticeEntity> noticeEntity = noticeRepository.findById(id);
        NoticeDto noticeDto = new NoticeDto();
        if (noticeEntity.isPresent()) {
            NoticeEntity notice = noticeEntity.get();
            noticeDto = notice.toDto();
        }
        return noticeDto;
    }

    public Page<NoticeEntity> getAllNotices(Pageable pageable) {
        Page<NoticeEntity> noticeEntities = noticeRepository.findAll(pageable);

        return noticeEntities;
    }

    public void updateNotice(NoticeDto noticeDto) { // 게시글 수정

        Optional<NoticeEntity> notice = noticeRepository.findById(noticeDto.getId());

        if (notice.isPresent()) {
            NoticeEntity oldNotice = notice.get(); // bbsdate, writer, bbsView 가져오기
            noticeDto.setCreateDate(oldNotice.toDto().getCreateDate()); // set date
            noticeDto.setViews(oldNotice.getViews()); // set bbsView
            noticeRepository.save(noticeDto.toEntity());
        }
    }

    public void updateViews(Long id) { // 조회수 업데이트
        NoticeEntity noticeEntity = noticeRepository.findById(id).get();
        noticeQueryRepository.updateViews(id, noticeEntity.getViews());
    }

    public void deleteNotice(Long id, List<String> urls) { // 게시글 삭제
        if (urls != null) {
            imageService.deleteUploadedImg(urls);
        }
        noticeRepository.deleteById(id);
    }
}
