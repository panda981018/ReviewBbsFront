package com.example.springsecuritytest.domain.repository.batch;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.example.springsecuritytest.domain.entity.QBatchResult.batchResult;
@Repository
@RequiredArgsConstructor
public class BatchQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public List<Tuple> findByYearAndMonth(String categoryName, int year, int month) {
        return jpaQueryFactory.select(batchResult.staticsDate, batchResult.bbsCount)
                .from(batchResult)
                .where(batchResult.staticsDate.year().eq(year)
                        .and(batchResult.staticsDate.month().eq(month))
                        .and(batchResult.name.eq(categoryName.toUpperCase())))
                .orderBy(batchResult.staticsDate.asc())
                .fetchResults()
                .getResults();
    }
}
