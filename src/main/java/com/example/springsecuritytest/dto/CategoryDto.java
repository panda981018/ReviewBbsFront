package com.example.springsecuritytest.dto;

import com.example.springsecuritytest.domain.category.CategoryEntity;
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

    public CategoryEntity toEntity() {
        return CategoryEntity.builder()
                .id(id)
                .name(name)
                .description(description)
                .build();
    }

    @Builder
    public CategoryDto(Long id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }
}