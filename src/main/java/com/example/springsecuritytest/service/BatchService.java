package com.example.springsecuritytest.service;

import com.example.springsecuritytest.domain.entity.BatchResult;
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

    private final BatchQueryRepository batchQueryRepository;
    private final BatchRepository batchRepository;

    // 년, 월, 카테고리명으로 검색
    public List<Object> getBatchDate(String category, int year, int month) {

        List<Tuple> batchResult = batchQueryRepository.findByYearAndMonth(category, year, month);
        List<Object> batchResultAboutCategory = new ArrayList<>();

        for (int i = 0; i< batchResult.size(); i++) {
            List<String> tmp = new ArrayList<>();
            tmp.add(batchResult.get(i).get(0, String.class)); // 날짜
            tmp.add(batchResult.get(i).get(1, String.class)); // count

            batchResultAboutCategory.add(tmp);
        }

        return batchResultAboutCategory;
    }

    // category 이름으로 검색
//    public HashMap<String, List> getBatchDate(String category) {
//        HashMap<String, List> batchHash = new HashMap<>();
//
//        List<Tuple> staticsList = batchQueryRepository.findByName(category);
//        List<String> dateString = new ArrayList<>();
//        List<Integer> countList = new ArrayList<>();
//        log.info("----- BatchService");
//
//        for (int i = 0; i < staticsList.size(); i++) {
//            dateString.add(staticsList.get(i).get(0, String.class));
//            countList.add(staticsList.get(i).get(1, Integer.class));
//        }
//
//        batchHash.put("dateString", dateString);
//        batchHash.put("countList", countList);
//
//        return batchHash;
//    }
}
