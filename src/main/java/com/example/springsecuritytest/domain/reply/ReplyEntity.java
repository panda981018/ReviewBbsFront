package com.example.springsecuritytest.domain.reply;

import com.example.springsecuritytest.domain.bbs.BbsEntity;
import com.example.springsecuritytest.domain.member.MemberEntity;
import com.example.springsecuritytest.dto.ReplyDto;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Getter
@Table(name = "JW_REPLY")
public class ReplyEntity {
    @Id
    @SequenceGenerator(
            name = "REPLY_SEQ_GEN",
            sequenceName = "JW_REPLY_SEQ",
            allocationSize = 1,
            initialValue = 1
    )
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

    @Column
    private String ipAddr;

    @ManyToOne
    @JoinColumn(name = "REPLY_WRITER")
    private MemberEntity writer;

    @ManyToOne
    @JoinColumn(name = "BBS_ID")
    private BbsEntity bbs;

    public void setWriter(MemberEntity member) { this.writer = member; }

    public void setBbs(BbsEntity bbs) { this.bbs = bbs; }

    public ReplyDto toDto() {

        String updateDateStr = "";
        boolean isUpdated = false;

        if (updateDate != null) {
            this.updateDate.toString().replace("T", " ");
            isUpdated = true;
        }
        return ReplyDto.builder()
                .id(id)
                .contents(contents)
                .createDate(createDate.toString().replace("T", " "))
                .updateDate(updateDateStr)
                .writer(writer.getNickname())
                .isUpdated(isUpdated)
                .bbs(bbs.getId())
                .build();
    }

    @Builder
    public ReplyEntity(Long id, String contents, LocalDateTime createDate, LocalDateTime updateDate, String ipAddr) {
        this.id = id;
        this.contents = contents;
        this.createDate = createDate;
        this.updateDate = updateDate;
        this.ipAddr = ipAddr;
    }
}