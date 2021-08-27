package com.example.springsecuritytest.dto;

import com.example.springsecuritytest.domain.entity.BbsEntity;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class BbsDto {

    private Long id;
    private String bbsTitle;
    private String bbsContents;
    private String bbsDate;
    private int bbsViews;

    public BbsEntity toEntity() {
        return BbsEntity.builder()
                .id(id)
                .bbsTitle(bbsTitle)
                .bbsContents(bbsContents)
                .bbsDate(bbsDate)
                .bbsViews(bbsViews)
                .build();
    }

    @Builder
    public BbsDto(Long id, String bbsTitle, String bbsContents, String bbsDate, int bbsViews) {
        this.id = id;
        this.bbsTitle = bbsTitle;
        this.bbsContents = bbsContents;
        this.bbsDate = bbsDate;
        this.bbsViews = bbsViews;
    }
}
