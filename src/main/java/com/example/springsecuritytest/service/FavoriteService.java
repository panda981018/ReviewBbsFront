package com.example.springsecuritytest.service;

import com.example.springsecuritytest.domain.entity.*;
import com.example.springsecuritytest.domain.repository.FavoriteRepository;
import com.example.springsecuritytest.domain.repository.MapRepository;
import com.example.springsecuritytest.domain.repository.bbs.BbsRepository;
import com.example.springsecuritytest.domain.repository.HeartRepository;
import com.example.springsecuritytest.domain.repository.member.MemberRepository;
import com.example.springsecuritytest.dto.FavoriteDto;
import com.example.springsecuritytest.dto.HeartDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final BbsRepository bbsRepository;
    private final MemberRepository memberRepository;
    private final MapRepository mapRepository;

    // 1. 존재여부 확인
    // 2-1. 존재한다면 기존 객체의 isLiked를 true로 변경
    // 2-2. 존재하지 않는다면 새로운 HeartEntity를 생성하여 isLiked를 true로 설정
    // 3. 마지막에 save

    // Optional의 .orElse()는 값이 있다면 값 리턴, 없다면 소괄호에 적은 코드 실행한 결과를 리턴.
    public String saveLocation(Long memberId, double latitude, double longitude, String placeName) {
        MapEntity mapEntity = mapRepository.findByLatitudeAndLongitude(latitude, longitude)
                .orElse(null);
        MemberEntity memberEntity = memberRepository.findById(memberId)
                .orElse(null);

        FavoriteDto favoriteDto = null;

        if (!favoriteRepository.existsByMemberAndMap(memberEntity, mapEntity)) { // 2-1의 경우
            favoriteDto = FavoriteDto.builder()
                    .latitude(latitude)
                    .longitude(longitude)
                    .placeName(placeName)
                    .mapId(mapEntity.getId())
                    .build();

            FavoriteEntity favEntity = favoriteDto.toEntity();
            favEntity.setMap(mapEntity);
            favEntity.setMember(memberEntity);

            favoriteRepository.save(favEntity);
        }
        return "OK";
    }

    public String cancelLocation(double latitude, double longitude, Long memberId) {
        MemberEntity memberEntity = memberRepository.findById(memberId).orElse(null);
        MapEntity mapEntity = mapRepository.findByLatitudeAndLongitude(latitude, longitude).orElse(null);
        FavoriteEntity favEntity = favoriteRepository.findByMemberAndMap(memberEntity, mapEntity).orElse(null);

        if (favEntity != null) {
            favoriteRepository.deleteById(favEntity.getId());
            return "OK";
        } else {
            return "FAIL";
        }
    }

    public boolean findFavObject(double latitude, double longitude, Long memberId) {
        MapEntity mapEntity = mapRepository.findByLatitudeAndLongitude(latitude, longitude).orElse(null);
        MemberEntity memberEntity = memberRepository.findById(memberId).orElse(null);

        return favoriteRepository.existsByMemberAndMap(memberEntity, mapEntity);
    }
}
