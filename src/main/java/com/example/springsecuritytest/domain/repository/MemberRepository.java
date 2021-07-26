package com.example.springsecuritytest.domain.repository;

import com.example.springsecuritytest.domain.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<MemberEntity, Long> {

    MemberEntity findByUsername(String username);

    // email 중복 여부 확인용
    // SignUpFormValidator에서 사용.
    boolean existsByUsername(String username);
    boolean existsByNickname(String nickname);

}
