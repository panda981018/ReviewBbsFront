package com.front.review.dto;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class HeartDto {

    private Long id;
    private boolean isLiked;

    @Builder
    public HeartDto(Long id, boolean isLiked) {
        this.id = id;
        this.isLiked = isLiked;
    }
}