package com.example.springsecuritytest.domain.entity;

import com.example.springsecuritytest.dto.FavoriteDto;
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

    @ManyToOne
    @JoinColumn(name = "mapId")
    private MapEntity map;

    @ManyToOne
    @JoinColumn(name = "memberId")
    private MemberEntity member;

    public void setMap(MapEntity map) {
        this.map = map;
    }

    public void setMember(MemberEntity member) {
        this.member = member;
    }

    public FavoriteDto toDto() {
        return FavoriteDto.builder()
                .id(id)
                .latitude(map.getLatitude())
                .longitude(map.getLongitude())
                .placeName(map.getPlaceName())
                .mapId(map.getId())
                .build();
    }

    @Builder
    public FavoriteEntity(Long id) {
        this.id = id;
    }
}
