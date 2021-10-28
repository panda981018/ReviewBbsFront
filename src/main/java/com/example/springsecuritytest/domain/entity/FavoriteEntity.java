package com.example.springsecuritytest.domain.entity;

import com.example.springsecuritytest.dto.FavoriteDto;
import com.example.springsecuritytest.dto.HeartDto;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@SequenceGenerator(
        name = "FAVORITE_SEQ_GEN",
        sequenceName = "FAVORITE_SEQ",
        allocationSize = 1
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "FAVORITE")
public class FavoriteEntity {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "FAVORITE_SEQ_GEN")
    private Long id;

    @Column
    private double latitude;

    @Column
    private double longitude;

    @Column
    private String placeName;

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

    public FavoriteDto toDto() {
        return FavoriteDto.builder()
                .id(id)
                .placeName(placeName)
                .latitude(latitude)
                .longitude(longitude)
                .build();
    }

    @Builder
    public FavoriteEntity(Long id, double latitude, double longitude, String placeName) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.placeName = placeName;
    }
}
