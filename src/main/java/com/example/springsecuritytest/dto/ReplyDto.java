package com.example.springsecuritytest.dto;

import com.example.springsecuritytest.domain.reply.ReplyEntity;
import lombok.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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

    public ReplyEntity toEntity() {
        if (updateDate != null) {
            String updateDateStr = this.updateDate.replace(" ", "T");

            return ReplyEntity.builder()
                    .id(id)
                    .contents(contents)
                    .createDate(LocalDateTime.parse(createDate.replace(" ", "T"), DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                    .updateDate(LocalDateTime.parse(updateDateStr, DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                    .ipAddr(ipAddr)
                    .build();
        } else {
            return ReplyEntity.builder()
                    .id(id)
                    .contents(contents)
                    .createDate(LocalDateTime.parse(createDate.replace(" ", "T"), DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                    .updateDate(null)
                    .ipAddr(ipAddr)
                    .build();
        }
    }

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
