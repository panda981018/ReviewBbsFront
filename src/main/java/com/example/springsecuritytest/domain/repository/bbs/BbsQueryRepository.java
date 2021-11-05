package com.example.springsecuritytest.domain.repository.bbs;

import com.example.springsecuritytest.domain.entity.BbsEntity;
import com.example.springsecuritytest.domain.entity.CategoryEntity;
import com.example.springsecuritytest.domain.entity.MemberEntity;
import com.example.springsecuritytest.domain.entity.QBbsEntity;
import com.example.springsecuritytest.util.QueryDslUtil;
import com.querydsl.core.QueryResults;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import groovy.util.OrderBy;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.example.springsecuritytest.domain.entity.QBbsEntity.bbsEntity;

@RequiredArgsConstructor
@Repository
public class BbsQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Transactional
    public void updateBbsViews(Long bbsId, int view) {
        jpaQueryFactory.update(bbsEntity)
                .set(bbsEntity.bbsViews, view + 1)
                .where(bbsEntity.id.eq(bbsId))
                .execute();
    }

//    @Transactional
//    public int plusLikeCount(Long bbsId, int likeCnt) {
//        jpaQueryFactory.update(bbsEntity)
//                .set(bbsEntity.likeCnt, likeCnt + 1)
//                .where(bbsEntity.id.eq(bbsId))
//                .execute();
//        return likeCnt + 1;
//    }
//
//    @Transactional
//    public int minusLikeCount(Long bbsId, int likeCnt) {
//        jpaQueryFactory.update(bbsEntity)
//                .set(bbsEntity.likeCnt, likeCnt - 1)
//                .where(bbsEntity.id.eq(bbsId))
//                .execute();
//        return likeCnt - 1;
//    }

    @Transactional
    public void plusLikeCount(Long bbsId, int likeCnt) {
        jpaQueryFactory.update(bbsEntity)
                .set(bbsEntity.likeCnt, likeCnt + 1)
                .where(bbsEntity.id.eq(bbsId))
                .execute();
    }

    @Transactional
    public void minusLikeCount(Long bbsId, int likeCnt) {
        jpaQueryFactory.update(bbsEntity)
                .set(bbsEntity.likeCnt, likeCnt - 1)
                .where(bbsEntity.id.eq(bbsId))
                .execute();
    }

    public Page<BbsEntity> findAllCategoryBbs(CategoryEntity category, Pageable pageable, String property) {

        List<OrderSpecifier> orders = new ArrayList<>();
        Order direction = pageable.getSort().getOrderFor(property)
                .getDirection().isAscending() ? Order.ASC : Order.DESC;
        OrderSpecifier order = QueryDslUtil.getSortedColumn(direction, bbsEntity, property);
        orders.add(order);

        QueryResults<BbsEntity> rt = jpaQueryFactory.selectFrom(bbsEntity)
                .where(bbsEntity.categoryId.eq(category))
                .orderBy(orders.stream().toArray(OrderSpecifier[]::new))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        return new PageImpl<>(rt.getResults(), pageable, rt.getTotal());
    }
//    try 1)
//    public List<BbsEntity> groupByCategory() {
//        return jpaQueryFactory.select(
//                        Projections.bean(BbsEntity.class,
//                                bbsEntity.categoryId,
//                                bbsEntity.categoryId.count().as("category_count"))
//                )
//                .from(bbsEntity)
//                .groupBy(bbsEntity.categoryId)
//                .fetchResults().getResults();
//    }

    // try 2) SUCCESS
    public List<Tuple> groupByCategory() {
        LocalDateTime startDate = LocalDateTime.parse("2021-11-05T14:10:00", DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        LocalDateTime endDate = LocalDateTime.parse("2021-11-05T14:20:59", DateTimeFormatter.ISO_LOCAL_DATE_TIME);

        return jpaQueryFactory.select(
                        bbsEntity.categoryId.name,
                        bbsEntity.categoryId.name.count()
                )
                .from(bbsEntity)
                .where(bbsEntity.bbsDate.between(startDate, endDate))
                .groupBy(bbsEntity.categoryId.name)
                .fetch();
    }
}