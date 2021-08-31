package com.example.springsecuritytest.domain.repository;

import com.example.springsecuritytest.domain.entity.BbsEntity;
import com.example.springsecuritytest.domain.entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BbsRepository extends JpaRepository<BbsEntity, Long> {

    Optional<List<BbsEntity>> findByCategoryId(CategoryEntity categoryId);
    Optional<BbsEntity> findById(Long id);
}
