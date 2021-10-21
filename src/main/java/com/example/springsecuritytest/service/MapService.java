package com.example.springsecuritytest.service;

import com.example.springsecuritytest.domain.repository.BbsQueryRepository;
import com.example.springsecuritytest.dto.MemberDto;
import com.querydsl.core.Tuple;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@AllArgsConstructor
public class MapService {

    private final BbsQueryRepository bbsQueryRepository;

    public Set<HashMap<String, Object>> getPlaceInfo(MemberDto memberDto) {
        List<Tuple> info = bbsQueryRepository.getMapElements(memberDto.toEntity());
        Set<HashMap<String, Object>> rtSet = new HashSet<>();

        for (int i = 0; i < info.size(); i++) {
            HashMap<String, Object> hashMap = new HashMap<>();
            Tuple tuple = (Tuple) info.get(i);
            if (tuple.get(0, Double.class) == 0.0) {
                continue;
            } else {
                hashMap.put("latitude", tuple.get(0, Double.class));
                hashMap.put("longitude", tuple.get(1, Double.class));
                hashMap.put("placeName", tuple.get(2, String.class));
            }
            rtSet.add(hashMap);
        }

        return rtSet;
    }
}
