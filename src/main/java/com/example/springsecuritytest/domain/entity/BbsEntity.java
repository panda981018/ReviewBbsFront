package com.example.springsecuritytest.domain.entity;

import com.example.springsecuritytest.dto.BbsDto;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@SequenceGenerator(
        name = "BBS_SEQ_GEN",
        sequenceName = "BBS_SEQ",
        allocationSize = 1
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Getter
@Table(name = "BBS")
public class BbsEntity {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "BBS_SEQ_GEN")
    private Long id;

    @Column(length = 100, nullable = false)
    private String bbsTitle;

    @Column(nullable = false)
    @Lob
    private String bbsContents;

    @Column(length = 25)
    private String bbsDate;

    private int bbsViews;

    @Column
    private int likeCnt;

    @Column
    private String ipAddr;

    @ManyToOne
    @JoinColumn(name = "CATEGORY_ID")
    private CategoryEntity categoryId; // 외래키

    @ManyToOne
    @JoinColumn(name = "BBS_WRITER")
    private MemberEntity bbsWriter;

    @OneToMany(mappedBy = "bbs", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<ReplyEntity> replies = new ArrayList<>(); // DB에 들어가지는 않는다.

    public void setCategory(CategoryEntity category) {
        this.categoryId = category;
    }

    public void setBbsWriter(MemberEntity member) {
        this.bbsWriter = member;
    }

    public void addReply(ReplyEntity replyEntity) {
        this.getReplies().add(replyEntity);
        replyEntity.setBbs(this);
    }

    public BbsDto toDto() {
        return BbsDto.builder()
                .id(id)
                .bbsTitle(bbsTitle)
                .bbsContents(bbsContents)
                .bbsDate(bbsDate)
                .categoryId(categoryId.getId())
                .bbsWriter(bbsWriter.getNickname())
                .bbsViews(bbsViews)
                .likeCnt(likeCnt)
                .replyCnt(replies.size())
                .ipAddr(ipAddr)
                .build();
    }

    @Builder
    public BbsEntity(Long id, String bbsTitle, String bbsContents, String bbsDate, int bbsViews,
                     int likeCnt, String ipAddr) {
        this.id = id;
        this.bbsTitle = bbsTitle;
        this.bbsContents = bbsContents;
        this.bbsDate = bbsDate;
        this.bbsViews = bbsViews;
        this.likeCnt = likeCnt;
        this.ipAddr = ipAddr;
    }
}
