package com.example.springsecuritytest.service;

import com.example.springsecuritytest.domain.entity.BbsEntity;
import com.example.springsecuritytest.domain.entity.ReplyEntity;
import com.example.springsecuritytest.domain.repository.BbsRepository;
import com.example.springsecuritytest.domain.repository.ReplyRepository;
import com.example.springsecuritytest.dto.MemberDto;
import com.example.springsecuritytest.dto.ReplyDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import javax.swing.text.html.Option;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@AllArgsConstructor
public class ReplyService {

    private final ReplyRepository replyRepository;
    private final BbsRepository bbsRepository;

    public void createReply(HttpSession session, ReplyDto replyDto) {

        LocalDateTime createTime = LocalDateTime.now();
        createTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        replyDto.setCreateDate(createTime); // 생성날짜 setting

        MemberDto replyWriter = (MemberDto) session.getAttribute("memberInfo");
        Optional<BbsEntity> bbsEntity = bbsRepository.findById(replyDto.getBbs());
        ReplyEntity replyEntity = replyDto.toEntity();
        replyEntity.setWriter(replyWriter.toEntity());
        replyEntity.setBbs(bbsEntity.get());

        bbsEntity.get().addReply(replyEntity);

        replyRepository.save(replyEntity);
    }

    public List<ReplyDto> getReplies(Long bbsId) {
        BbsEntity bbsEntity = bbsRepository.findById(bbsId).isPresent() ? bbsRepository.findById(bbsId).get() : null;

        List<ReplyEntity> replies = bbsEntity.getReplies();
//        List<ReplyEntity> replyEntities = replyRepository.findByBbs(bbsEntity);
        List<ReplyDto> replyDtoList = new ArrayList<>();

        for (int i = 0; i < replies.size(); i++) {
            replyDtoList.add(replies.get(i).toDto());
        }
        return replyDtoList;
    }

    public void updateReply(ReplyDto replyDto) {

        LocalDateTime updateNow = LocalDateTime.now();
        updateNow.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        replyDto.setUpdateDate(updateNow);

        replyRepository.save(replyDto.toEntity());
    }

    public void removeReply(Long replyId, Long bbsId) {
        Optional<ReplyEntity> reply = replyRepository.findById(replyId);
        Optional<BbsEntity> bbs = bbsRepository.findById(bbsId);
        BbsEntity bbsEntity = bbs.get();
        bbsEntity.getReplies().remove(reply);
    }
}
