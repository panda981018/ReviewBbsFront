package com.example.springsecuritytest.dto;

import com.example.springsecuritytest.domain.entity.BbsEntity;
import lombok.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static com.example.springsecuritytest.conf.AppConfig.stringToLocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class BbsDto {

    private Long id; // 고유번호
    private String bbsTitle; // 제목
    private String bbsContents; // 작성내용
    private String bbsDate; // 작성날짜
    private String bbsWriter; // 작성자 닉네임
    private Long categoryId; // 카테고리 번호
    private int bbsViews; // 조회수
    private int replyCnt = 0; // 댓글 수
    private int likeCnt; //  좋아요 수
    private String ipAddr; // 작성자 ip주소
    private double latitude = 0.0; // 위도
    private double longitude = 0.0; // 경도
    private String placeName;

    public BbsEntity toEntity() {
        return BbsEntity.builder()
                .id(id)
                .bbsTitle(bbsTitle)
                .bbsContents(bbsContents)
                .bbsDate(stringToLocalDateTime(this.bbsDate))
                .bbsViews(bbsViews)
                .likeCnt(likeCnt)
                .ipAddr(ipAddr)
                .build();
    }

    @Builder
    public BbsDto(Long id, String bbsTitle, String bbsContents, String bbsDate,
                  int bbsViews, int likeCnt, Long categoryId, String bbsWriter, int replyCnt, String ipAddr,
                  double latitude, double longitude, String placeName) {
        this.id = id;
        this.bbsTitle = bbsTitle;
        this.bbsContents = bbsContents;
        this.bbsDate = bbsDate;
        this.bbsViews = bbsViews;
        this.categoryId = categoryId;
        this.bbsWriter = bbsWriter;
        this.likeCnt = likeCnt;
        this.replyCnt = replyCnt;
        this.ipAddr = ipAddr;
        this.latitude = latitude;
        this.longitude = longitude;
        this.placeName = placeName;
    }
}
