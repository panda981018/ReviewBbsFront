package com.example.springsecuritytest.domain.repository;

import com.example.springsecuritytest.domain.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<MemberEntity, Long> {

    // email을 where 절로 하여, 데이터를 가져올 수 있도록 findByEmail() 메서드를 정의함.
    Optional<MemberEntity> findByEmail(String userEmail);

}
