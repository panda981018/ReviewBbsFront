package com.example.springsecuritytest.dto;

import com.example.springsecuritytest.domain.entity.ReplyEntity;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class ReplyDto {

    private Long id;
    private String contents;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;
    private String writer;
    private Long bbs;

    public ReplyEntity toEntity() {
        return ReplyEntity.builder()
                .id(id)
                .contents(contents)
                .createDate(createDate)
                .updateDate(updateDate)
                .build();
    }

    @Builder
    public ReplyDto(Long id, String contents, LocalDateTime createDate, LocalDateTime updateDate, String writer, Long bbs) {
        this.id = id;
        this.contents = contents;
        this.createDate = createDate;
        this.updateDate = updateDate;
        this.writer = writer;
        this.bbs = bbs;
    }
}
