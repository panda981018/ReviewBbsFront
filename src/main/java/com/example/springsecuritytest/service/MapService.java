package com.example.springsecuritytest.service;

import com.example.springsecuritytest.domain.entity.BbsEntity;
import com.example.springsecuritytest.domain.entity.FavoriteEntity;
import com.example.springsecuritytest.domain.entity.MapEntity;
import com.example.springsecuritytest.domain.repository.FavoriteRepository;
import com.example.springsecuritytest.domain.repository.MapRepository;
import com.example.springsecuritytest.domain.repository.bbs.BbsQueryRepository;
import com.example.springsecuritytest.domain.repository.bbs.BbsRepository;
import com.example.springsecuritytest.dto.BbsDto;
import com.example.springsecuritytest.dto.FavoriteDto;
import com.example.springsecuritytest.dto.MemberDto;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@Service
@AllArgsConstructor
public class MapService {

    private final FavoriteRepository favoriteRepository;
    private final MapRepository mapRepository;

    public HashMap<String, Object> getPlaceInfo(MemberDto memberDto, Pageable pageable) {
        HashMap<String, Object> pagingMap = new HashMap<>();
        // dto 리스트, total 넘기기
        Page<FavoriteEntity> favEntities = favoriteRepository.findByMember(memberDto.toEntity(), pageable);
        List<FavoriteDto> favDtos = new ArrayList<>();

        for (FavoriteEntity fav : favEntities.getContent()) { // favEntity { id, map(외래키), member(외래키) }
            FavoriteDto favDto = fav.toDto();
            favDtos.add(favDto);
        }
        pagingMap.put("data", favDtos);
        pagingMap.put("totalPages", favEntities.getTotalPages());

        return pagingMap;
    }

    public List<BbsDto> getBbsList(double lat, double lng) {

        MapEntity mapEntity = mapRepository.findByLatitudeAndLongitude(lat, lng)
                .orElse(null);

        List<BbsDto> bbsDtoList = new ArrayList<>();
        if (mapEntity != null) {
            List<BbsEntity> bbsEntityList = mapEntity.getBbsEntityList();
            for (BbsEntity bbs : bbsEntityList) {
                bbsDtoList.add(bbs.toDto());
            }
        }
        return bbsDtoList;
    }
}
