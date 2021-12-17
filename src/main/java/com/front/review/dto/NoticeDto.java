package com.front.review.dto;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class NoticeDto {

    private Long id;
    private String title;
    private String contents;
    private String createDate;
    private int views;
    private String writerRole;

    @Builder
    public NoticeDto(Long id, String title, String contents, String createDate, int views, String writerRole) {
        this.id = id;
        this.title = title;
        this.contents = contents;
        this.createDate = createDate;
        this.views = views;
        this.writerRole = writerRole;
    }
}
