package com.example.springsecuritytest.dto;

import com.example.springsecuritytest.domain.entity.FavoriteEntity;
import com.example.springsecuritytest.domain.entity.HeartEntity;
import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class FavoriteDto {

    private Long id;
    private double latitude;
    private double longitude;
    private String placeName;
    private Long mapId;

    public FavoriteEntity toEntity() {
        return FavoriteEntity.builder()
                .id(id)
                .build();
    }

    @Builder
    public FavoriteDto(Long id, double latitude, double longitude, String placeName, Long mapId) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.placeName = placeName;
        this.mapId = mapId;
    }
}