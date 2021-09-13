package com.example.springsecuritytest.domain.repository;

import com.example.springsecuritytest.domain.entity.BbsEntity;
import com.example.springsecuritytest.domain.entity.CategoryEntity;
import com.example.springsecuritytest.domain.entity.QBbsEntity;
import com.example.springsecuritytest.util.QueryDslUtil;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.example.springsecuritytest.domain.entity.QBbsEntity.bbsEntity;
import static com.example.springsecuritytest.domain.entity.QMemberEntity.memberEntity;

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

    public Page<BbsEntity> findAllCategoryBbs(CategoryEntity category, Pageable pageable, List<String> properties) {

        List<OrderSpecifier> orders = new ArrayList<>();

        for (int i = properties.size()-1; i >= 0; i--) {
            Order direction = pageable.getSort().getOrderFor(properties.get(i))
                    .getDirection().isAscending() ? Order.ASC : Order.DESC;
            OrderSpecifier order = QueryDslUtil.getSortedColumn(direction, bbsEntity, properties.get(i));
            orders.add(order);
        }


        QueryResults<BbsEntity> rt = jpaQueryFactory.selectFrom(bbsEntity)
                .where(bbsEntity.categoryId.eq(category))
                .orderBy(orders.stream().toArray(OrderSpecifier[]::new))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        return new PageImpl<>(rt.getResults(), pageable, rt.getTotal());
    }
}