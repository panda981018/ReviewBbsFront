package com.example.springsecuritytest.domain.repository;

import com.example.springsecuritytest.domain.entity.MemberEntity;
import com.example.springsecuritytest.dto.MemberDto;
import com.example.springsecuritytest.dto.SignUpForm;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;

import static com.example.springsecuritytest.domain.entity.QMemberEntity.memberEntity;

@RequiredArgsConstructor
@Repository
public class MemberQueryRepository { // CRUD

    private final JPAQueryFactory jpaQueryFactory;

    // update
    @Modifying
    @Transactional
    public void updateUserInfo(MemberDto memberDto) throws SQLException {
        if (memberDto.getPassword() == null) {
            jpaQueryFactory.update(memberEntity)
                    .set(memberEntity.nickname, memberDto.getNickname())
                    .set(memberEntity.age, memberDto.getAge())
                    .where(memberEntity.username.eq(memberDto.getUsername()))
                    .execute();
        } else {
            jpaQueryFactory.update(memberEntity)
                    .set(memberEntity.password, memberDto.getPassword())
                    .set(memberEntity.nickname, memberDto.getNickname())
                    .set(memberEntity.age, memberDto.getAge())
                    .where(memberEntity.username.eq(memberDto.getUsername()))
                    .execute();
        }
    }

    // delete
    @Transactional
    public void deleteUser(String username) throws SQLException {

    }

}
