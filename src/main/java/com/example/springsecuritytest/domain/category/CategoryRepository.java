package com.example.springsecuritytest.domain.category;

import com.example.springsecuritytest.domain.category.CategoryEntity;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {

    Optional<CategoryEntity> findById(Long id);

    List<CategoryEntity> findAll(Sort sort);
}
