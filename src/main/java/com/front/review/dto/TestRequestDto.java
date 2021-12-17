package com.front.review.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class TestRequestDto {
    private int id;
    private String msg;

    @Builder
    public TestRequestDto(int id, String msg) {
        this.id = id;
        this.msg = msg;
    }
}
