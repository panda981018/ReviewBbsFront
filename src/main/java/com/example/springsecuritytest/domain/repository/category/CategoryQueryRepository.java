package com.example.springsecuritytest.domain.repository.category;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.example.springsecuritytest.domain.entity.QCategoryEntity.categoryEntity;

@Repository
@RequiredArgsConstructor
public class CategoryQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public List<String> findAllCategoryName() {
        return jpaQueryFactory.select(categoryEntity.name)
                .from(categoryEntity)
                .orderBy(categoryEntity.id.asc())
                .fetchResults()
                .getResults();
    }
}
