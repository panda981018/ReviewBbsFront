package com.example.springsecuritytest.service;

import com.example.springsecuritytest.domain.repository.batch.BatchQueryRepository;
import com.example.springsecuritytest.domain.repository.category.CategoryQueryRepository;
import com.querydsl.core.Tuple;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class BatchService {

    private final BatchQueryRepository batchQueryRepository;

    public List<Object> getBatchDate(String category, int year, int month) {

        List<Tuple> batchResult = batchQueryRepository.findByYearAndMonth(category, year, month);
        List<Object> batchResultAboutCategory = new ArrayList<>();

        for (int i = 0; i< batchResult.size(); i++) {
            List<Object> tmp = new ArrayList<>();
            tmp.add(batchResult.get(i).get(0, String.class)); // 날짜
            tmp.add(batchResult.get(i).get(1, Integer.class)); // count

            batchResultAboutCategory.add(tmp);
        }

        return batchResultAboutCategory;
    }
}
