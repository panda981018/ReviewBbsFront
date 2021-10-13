package com.example.springsecuritytest.domain.repository;

import com.example.springsecuritytest.domain.entity.BbsEntity;
import com.example.springsecuritytest.domain.entity.HeartEntity;
import com.example.springsecuritytest.domain.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HeartRepository extends JpaRepository<HeartEntity, Long> {

    Optional<HeartEntity> findById(Long id);
    Optional<HeartEntity> findByBbsAndMember(BbsEntity bbs, MemberEntity member);
    boolean existsByBbsAndMember(BbsEntity bbs, MemberEntity member);
}
