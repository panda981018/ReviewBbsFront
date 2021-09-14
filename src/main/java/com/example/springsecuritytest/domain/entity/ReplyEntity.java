package com.example.springsecuritytest.domain.entity;

import com.example.springsecuritytest.dto.ReplyDto;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@SequenceGenerator(
        name = "REPLY_SEQ_GEN",
        sequenceName = "REPLY_SEQ",
        allocationSize = 1
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Getter
@Table(name = "REPLY")
public class ReplyEntity {
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "REPLY_SEQ_GEN")
    private Long id;

    @Column(length = 150, nullable = false)
    private String contents;

    @Column(length = 20)
    private LocalDateTime createDate;

    @Column(length = 20)
    private LocalDateTime updateDate;

    @ManyToOne
    @JoinColumn(name = "REPLY_WRITER")
    private MemberEntity writer;

    @ManyToOne
    @JoinColumn(name = "BBS_ID")
    private BbsEntity bbs;

    public void setWriter(MemberEntity member) { this.writer = member; }

    public void setBbs(BbsEntity bbs) { this.bbs = bbs; }

    public ReplyDto toDto() {
        return ReplyDto.builder()
                .id(id)
                .contents(contents)
                .createDate(createDate)
                .updateDate(updateDate)
                .writer(writer.getNickname())
                .bbs(bbs.getId())
                .build();
    }

    @Builder
    public ReplyEntity(Long id, String contents, LocalDateTime createDate, LocalDateTime updateDate) {
        this.id = id;
        this.contents = contents;
        this.createDate = createDate;
        this.updateDate = updateDate;
    }

}
