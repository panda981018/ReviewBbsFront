package com.example.springsecuritytest.dto;

import com.example.springsecuritytest.domain.entity.NoticeEntity;
import lombok.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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

    public NoticeEntity toEntity() {
        return NoticeEntity.builder()
                .id(id)
                .title(title)
                .contents(contents)
                .createDate(LocalDateTime.parse(createDate.replace(" ", "T"), DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                .views(views)
                .writerRole(writerRole)
                .build();
    }

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
