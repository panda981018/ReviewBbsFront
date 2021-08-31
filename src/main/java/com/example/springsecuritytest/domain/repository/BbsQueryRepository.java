package com.example.springsecuritytest.domain.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

import static com.example.springsecuritytest.domain.entity.QBbsEntity.bbsEntity;

@RequiredArgsConstructor
@Repository
public class BbsQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Transactional
    public void updateBbsViews(Long bbsId, int view) {
        jpaQueryFactory.update(bbsEntity)
                .set(bbsEntity.bbsViews, view+1)
                .where(bbsEntity.id.eq(bbsId))
                .execute();
    }
}
