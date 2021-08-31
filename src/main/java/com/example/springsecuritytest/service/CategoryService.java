package com.example.springsecuritytest.service;

import com.example.springsecuritytest.domain.entity.CategoryEntity;
import com.example.springsecuritytest.domain.repository.CategoryRepository;
import com.example.springsecuritytest.dto.CategoryDto;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public void createCategory(CategoryDto categoryDto) {
        categoryDto.setName(categoryDto.getName().toUpperCase());
        categoryRepository.save(categoryDto.toEntity());
    }

    public List<CategoryDto> getAllCategories() {
        List<CategoryEntity> categoryEntities = categoryRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
        List<CategoryDto> categories = new ArrayList<>();

        for (CategoryEntity category : categoryEntities) {
            categories.add(category.toDto());
        }

        return categories;
    }
}
