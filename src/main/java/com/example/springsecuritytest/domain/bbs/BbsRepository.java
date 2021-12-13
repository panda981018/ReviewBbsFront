package com.example.springsecuritytest.domain.bbs;

import com.example.springsecuritytest.domain.bbs.BbsEntity;
import com.example.springsecuritytest.domain.category.CategoryEntity;
import com.example.springsecuritytest.domain.member.MemberEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BbsRepository extends JpaRepository<BbsEntity, Long> {

    // 제목 검색
    Page<BbsEntity> findByCategoryIdAndBbsTitleContainingIgnoreCase(Pageable pageable, CategoryEntity category, String keyword);
    // 작성자 검색
    Page<BbsEntity> findByCategoryIdAndBbsWriter(Pageable pageable, CategoryEntity category, MemberEntity member);
    Page<BbsEntity> findByCategoryId(CategoryEntity category, Pageable pageable);
    // 카테고리 상관없이 모두 찾기
    Page<BbsEntity> findAll(Pageable pageable);
    Optional<BbsEntity> findById(Long id);
    void deleteById(Long id);
}
