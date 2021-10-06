package com.example.springsecuritytest.domain.repository;

import com.example.springsecuritytest.domain.entity.MemberEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<MemberEntity, Long> {

    Optional<MemberEntity> findByUsername(String username);
    Optional<MemberEntity> findByNickname(String nickname);
    boolean existsByUsername(String username);
    boolean existsByNickname(String nickname);
}
