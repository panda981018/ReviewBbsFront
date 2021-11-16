package com.example.springsecuritytest.service;

import com.example.springsecuritytest.domain.entity.BatchResult;
import com.example.springsecuritytest.domain.repository.batch.BatchQueryRepository;
import com.example.springsecuritytest.domain.repository.batch.BatchRepository;
import com.querydsl.core.Tuple;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class BatchService {

    private final BatchQueryRepository batchQueryRepository;
    private final BatchRepository batchRepository;

    public void getBatchDate(String category) {
        List<Tuple> dateArr = batchQueryRepository.findByName(category);
        log.info("----- BatchService");
        for (int i = 0; i < dateArr.size(); i++) {
            log.info("{}", dateArr.get(i));
        }
//        for (int i = 0; i < dateArr.size(); i++) {
//            log.info("date = {}", dateArr.get(i).get(0, String.class));
//            log.info("name = {}", dateArr.get(i).get(1, String.class));
//        }
    }
}
