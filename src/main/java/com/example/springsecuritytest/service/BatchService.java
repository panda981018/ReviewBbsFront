package com.example.springsecuritytest.service;

import com.example.springsecuritytest.domain.repository.category.CategoryQueryRepository;
import com.example.springsecuritytest.domain.repository.category.CategoryRepository;
import com.example.springsecuritytest.domain.repository.batch.BatchQueryRepository;
import com.example.springsecuritytest.domain.repository.batch.BatchRepository;
import com.querydsl.core.Tuple;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class BatchService {

    private final CategoryQueryRepository categoryQueryRepository;
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
