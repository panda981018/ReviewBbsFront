package com.example.springsecuritytest.domain.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

import static com.example.springsecuritytest.domain.entity.QNoticeEntity.noticeEntity;

@RequiredArgsConstructor
@Repository
public class NoticeQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Transactional
    public void updateViews(Long noticeId, int view) {
        jpaQueryFactory.update(noticeEntity)
                .set(noticeEntity.views, view+1)
                .where(noticeEntity.id.eq(noticeId))
                .execute();
    }
}