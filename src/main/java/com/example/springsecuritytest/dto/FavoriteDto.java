package com.example.springsecuritytest.dto;

import com.example.springsecuritytest.domain.entity.FavoriteEntity;
import com.example.springsecuritytest.domain.entity.HeartEntity;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class FavoriteDto {

    private Long id;
    private double latitude;
    private double longitude;
    private String placeName;
    private Long bbsId;
    private Long memberId;

    public FavoriteEntity toEntity() {
        return FavoriteEntity.builder()
                .id(id)
                .placeName(placeName)
                .latitude(latitude)
                .longitude(longitude)
                .build();
    }

    @Builder
    public FavoriteDto(Long id, double latitude, double longitude, String placeName) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.placeName = placeName;
    }
}