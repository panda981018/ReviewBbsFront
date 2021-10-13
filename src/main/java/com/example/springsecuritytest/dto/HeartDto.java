package com.example.springsecuritytest.dto;

import com.example.springsecuritytest.domain.entity.HeartEntity;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class HeartDto {

    private Long id;
    private boolean isLiked;

    public HeartEntity toEntity() {
        return HeartEntity.builder()
                .id(id)
                .isLiked(isLiked)
                .build();
    }

    @Builder
    public HeartDto(Long id, boolean isLiked) {
        this.id = id;
        this.isLiked = isLiked;
    }
}