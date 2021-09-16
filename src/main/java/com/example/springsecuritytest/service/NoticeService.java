package com.example.springsecuritytest.service;

import com.example.springsecuritytest.domain.entity.BbsEntity;
import com.example.springsecuritytest.domain.entity.CategoryEntity;
import com.example.springsecuritytest.domain.entity.NoticeEntity;
import com.example.springsecuritytest.domain.repository.*;
import com.example.springsecuritytest.dto.BbsDto;
import com.example.springsecuritytest.dto.MemberDto;
import com.example.springsecuritytest.dto.NoticeDto;
import lombok.AllArgsConstructor;
import org.apache.commons.io.FileUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@AllArgsConstructor
public class NoticeService {

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
            deleteUploadedImg(urls);
        }
        noticeRepository.deleteById(id);
    }

    public void deleteUploadedImg(List<String> urls) { // 게시글에 포함된 이미지 삭제
        for (String url : urls) {
            FileUtils.deleteQuietly(new File(url));
        }
    }

    public HashMap<String, String> uploadImage(List<MultipartFile> multipartFile) { // 이미지 업로드
        HashMap<String, String> data = new HashMap<>();

        String fileRoot = "D:\\summernoteImg\\"; // 저장될 경로

        for (int i = 0; i < multipartFile.size(); i++) {
            String originalFileName = multipartFile.get(i).getOriginalFilename();
            String type = originalFileName.substring(originalFileName.lastIndexOf("."));

            String savedFileName = UUID.randomUUID() + type;

            File targetFile = new File(fileRoot + savedFileName);

            try {
                InputStream fileStream = multipartFile.get(i).getInputStream();
                FileUtils.copyInputStreamToFile(fileStream, targetFile); // inputstream, 파일저장경로
                data.put("url", "/summernoteImg/" + savedFileName);
                data.put("responseCode", "success");
            } catch (IOException e) {
                FileUtils.deleteQuietly(targetFile); // 에러나면 저장된 파일 삭제
                data.put("responseCode", "error");
                e.printStackTrace();
            }
        }

        return data;
    }
}
