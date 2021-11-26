package com.example.springsecuritytest.domain.entity;

import com.example.springsecuritytest.dto.HeartDto;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "JW_HEART")
public class HeartEntity {

    @Id
    @SequenceGenerator(
            name = "HEART_SEQ_GEN",
            sequenceName = "JW_HEART_SEQ",
            allocationSize = 1,
            initialValue = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "HEART_SEQ_GEN")
    private Long id;

    @Column
    private boolean isLiked;

    @ManyToOne
    @JoinColumn(name = "bbsId")
    private BbsEntity bbs;

    @ManyToOne
    @JoinColumn(name = "memberId")
    private MemberEntity member;

    public void setBbs(BbsEntity bbs) {
        this.bbs = bbs;
    }

    public void setMember(MemberEntity member) {
        this.member = member;
    }

    public HeartDto toDto() {
        return HeartDto.builder()
                .id(id)
                .isLiked(isLiked)
                .build();
    }

    @Builder
    public HeartEntity(Long id, boolean isLiked) {
        this.id = id;
        this.isLiked = isLiked;
    }
}
