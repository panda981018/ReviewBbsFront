package com.example.springsecuritytest.domain.repository;

import com.example.springsecuritytest.domain.entity.BbsEntity;
import com.example.springsecuritytest.domain.entity.FavoriteEntity;
import com.example.springsecuritytest.domain.entity.MemberEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FavoriteRepository extends JpaRepository<FavoriteEntity, Long> {

    Optional<FavoriteEntity> findById(Long id);
    Optional<FavoriteEntity> findByBbsAndMember(BbsEntity bbs, MemberEntity member);
    Page<FavoriteEntity> findByMember(MemberEntity membere, Pageable pageable);
    boolean existsByBbsAndMember(BbsEntity bbs, MemberEntity member);
}
