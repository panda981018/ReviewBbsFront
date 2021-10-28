package com.example.springsecuritytest.domain.repository.member;

import com.example.springsecuritytest.domain.entity.MemberEntity;
import com.example.springsecuritytest.enumclass.Role;
import com.example.springsecuritytest.util.QueryDslUtil;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.List;

import static com.example.springsecuritytest.domain.entity.QMemberEntity.memberEntity;

@RequiredArgsConstructor
@Repository
public class MemberQueryRepository { // CRUD

    private final JPAQueryFactory jpaQueryFactory;

    public QueryResults<String> findByNickname(Long id) throws SQLException {

        QueryResults<String> rtResult = jpaQueryFactory.select(memberEntity.nickname)
                .from(memberEntity)
                .where(memberEntity.id.ne(id))
                .fetchResults();

        return rtResult;
    }

    public Page<MemberEntity> findAllExceptAdmin(Pageable pageable) {
        List<OrderSpecifier> order = QueryDslUtil.getAllOrderSpecifiers(pageable);

        QueryResults<MemberEntity> rt = jpaQueryFactory.selectFrom(memberEntity)
                .where(memberEntity.role.ne(Role.ADMIN))
                .orderBy(order.stream().toArray(OrderSpecifier[]::new))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        return new PageImpl<>(rt.getResults(), pageable, rt.getTotal());
    }

    // delete
    @Transactional
    public void deleteUser(String username) throws SQLException {

    }

}
