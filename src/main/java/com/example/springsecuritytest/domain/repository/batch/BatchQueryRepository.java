package com.example.springsecuritytest.domain.repository.batch;

import com.querydsl.core.QueryResults;
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

//    public List<Tuple> getBatchDate() {
//        return jpaQueryFactory.select(batchResult.staticsDate, batchResult.name)
//                .from(batchResult)
//                .groupBy(batchResult.staticsDate, batchResult.name)
//                .fetch();
//    }

    public List<Tuple> findByName(String category) {
        return jpaQueryFactory.select(batchResult.staticsDate, batchResult.bbsCount)
                .from(batchResult)
                .where(batchResult.name.eq(category))
                .fetchResults()
                .getResults();
    }
}
