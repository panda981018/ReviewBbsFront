package com.front.review.dto;

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
    private Long mapId;

    @Builder
    public FavoriteDto(Long id, double latitude, double longitude, String placeName, Long mapId) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.placeName = placeName;
        this.mapId = mapId;
    }
}