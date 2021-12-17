package com.front.review.dto;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class CategoryDto implements Serializable {

    private Long id;
    private String name;
    private String description;

    @Builder
    public CategoryDto(Long id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }
}