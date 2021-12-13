package com.example.springsecuritytest.domain.heart;

import com.example.springsecuritytest.domain.bbs.BbsEntity;
import com.example.springsecuritytest.domain.heart.HeartEntity;
import com.example.springsecuritytest.domain.member.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HeartRepository extends JpaRepository<HeartEntity, Long> {

    Optional<HeartEntity> findById(Long id);
    Optional<HeartEntity> findByBbsAndMember(BbsEntity bbs, MemberEntity member);
    List<HeartEntity> findByBbs(BbsEntity bbs);
    boolean existsByBbsAndMember(BbsEntity bbs, MemberEntity member);
}
