package com.example.springsecuritytest.domain.notice;

import com.example.springsecuritytest.dto.NoticeDto;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.bind.DefaultValue;

import javax.persistence.*;
import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Getter
@Table(name = "JW_NOTICE")
public class NoticeEntity {

    @Id
    @SequenceGenerator(
            name = "NOTICE_SEQ_GEN",
            sequenceName = "JW_NOTICE_SEQ",
            allocationSize = 1,
            initialValue = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "NOTICE_SEQ_GEN")
    private Long id;

    @Column(length = 50, nullable = false)
    private String title;

    @Column(nullable = false)
    @Lob
    private String contents;

    @Column(nullable = false)
    private LocalDateTime createDate;

    private int views;
    private String writerRole;

    public NoticeDto toDto() {
        return NoticeDto.builder()
                .id(id)
                .title(title)
                .contents(contents)
                .createDate(createDate.toString().replace("T", " "))
                .views(views)
                .writerRole(writerRole)
                .build();
    }

    @Builder
    public NoticeEntity(Long id, String title, String contents, LocalDateTime createDate, int views, String writerRole) {
        this.id = id;
        this.title = title;
        this.contents = contents;
        this.createDate = createDate;
        this.views = views;
        this.writerRole = writerRole;
    }
}
