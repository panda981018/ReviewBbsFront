package com.example.springsecuritytest.domain.entity;

import com.example.springsecuritytest.dto.BbsDto;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.example.springsecuritytest.conf.AppConfig.localDateTimeToString;

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

    @Column
    private LocalDateTime bbsDate;

    @Column
    private int bbsViews;

    @Column
    private int likeCnt;

    @Column
    private String ipAddr; // 작성자의 ip주소

    @ManyToOne
    @JoinColumn(name = "CATEGORY_ID")
    private CategoryEntity categoryId; // 외래키

    @ManyToOne
    @JoinColumn(name = "BBS_WRITER")
    private MemberEntity bbsWriter;

    @ManyToOne
    @JoinColumn(name = "LOCATION")
    private MapEntity map; // 게시물에서 위치를 지정한 경우

    @OneToMany(mappedBy = "bbs", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<ReplyEntity> replies = new ArrayList<>(); // DB에 들어가지는 않는다.

    public void setCategory(CategoryEntity category) {
        this.categoryId = category;
    }

    public void setBbsWriter(MemberEntity member) {
        this.bbsWriter = member;
    }

    public void setMap(MapEntity map) { this.map = map; }

    public void addReply(ReplyEntity replyEntity) {
        this.getReplies().add(replyEntity);
        replyEntity.setBbs(this);
    }

    public BbsDto toDto() {

        String writeDate = localDateTimeToString(this.bbsDate);

        if (map != null) {
            return BbsDto.builder()
                    .id(id)
                    .bbsTitle(bbsTitle)
                    .bbsContents(bbsContents)
                    .bbsDate(writeDate)
                    .categoryId(categoryId.getId())
                    .bbsWriter(bbsWriter.getNickname())
                    .bbsViews(bbsViews)
                    .likeCnt(likeCnt)
                    .replyCnt(replies.size())
                    .ipAddr(ipAddr)
                    .latitude(map.getLatitude())
                    .longitude(map.getLongitude())
                    .placeName(map.getPlaceName())
                    .build();
        } else {
            return BbsDto.builder()
                    .id(id)
                    .bbsTitle(bbsTitle)
                    .bbsContents(bbsContents)
                    .bbsDate(writeDate)
                    .categoryId(categoryId.getId())
                    .bbsWriter(bbsWriter.getNickname())
                    .bbsViews(bbsViews)
                    .likeCnt(likeCnt)
                    .replyCnt(replies.size())
                    .ipAddr(ipAddr)
                    .latitude(0.0)
                    .longitude(0.0)
                    .placeName("")
                    .build();
        }

    }

    @Builder
    public BbsEntity(Long id, String bbsTitle, String bbsContents, LocalDateTime bbsDate, int bbsViews,
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
