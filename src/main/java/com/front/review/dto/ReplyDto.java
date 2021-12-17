package com.front.review.dto;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class ReplyDto {

    private Long id;
    private String contents;
    private String createDate;
    private String updateDate;
    private boolean isUpdated;
    private String writer;
    private Long bbs;
    private String ipAddr;

    @Builder
    public ReplyDto(Long id, String contents, String createDate, String updateDate, String writer, Long bbs, boolean isUpdated, String ipAddr) {
        this.id = id;
        this.contents = contents;
        this.createDate = createDate;
        this.updateDate = updateDate;
        this.isUpdated = isUpdated;
        this.writer = writer;
        this.bbs = bbs;
        this.ipAddr = ipAddr;
    }
}
