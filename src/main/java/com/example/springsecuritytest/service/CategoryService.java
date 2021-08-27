package com.example.springsecuritytest.service;

import com.example.springsecuritytest.domain.repository.CategoryRepository;
import com.example.springsecuritytest.dto.CategoryDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public void createCategory(CategoryDto categoryDto) {
        categoryDto.setName(categoryDto.getName().toUpperCase());
        categoryRepository.save(categoryDto.toEntity());
    }
}
