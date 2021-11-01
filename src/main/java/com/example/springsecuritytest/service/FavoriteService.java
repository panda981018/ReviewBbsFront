package com.example.springsecuritytest.service;

import com.example.springsecuritytest.domain.entity.BbsEntity;
import com.example.springsecuritytest.domain.entity.FavoriteEntity;
import com.example.springsecuritytest.domain.entity.HeartEntity;
import com.example.springsecuritytest.domain.entity.MemberEntity;
import com.example.springsecuritytest.domain.repository.FavoriteRepository;
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

    // 1. 존재여부 확인
    // 2-1. 존재한다면 기존 객체의 isLiked를 true로 변경
    // 2-2. 존재하지 않는다면 새로운 HeartEntity를 생성하여 isLiked를 true로 설정
    // 3. 마지막에 save

    // Optional의 .orElse()는 값이 있다면 값 리턴, 없다면 소괄호에 적은 코드 실행한 결과를 리턴.
    public String saveLocation(Long bid, Long memberId, double latitude, double longitude, String placeName) {
        BbsEntity bbsEntity = bbsRepository.findById(bid)
                .orElse(null);
        MemberEntity memberEntity = memberRepository.findById(memberId)
                .orElse(null);

        FavoriteEntity favoriteEntity = null;

        if (favoriteRepository.existsByBbsAndMember(bbsEntity, memberEntity)) { // 2-1의 경우
            FavoriteEntity oldFavEntity = favoriteRepository.findByBbsAndMember(bbsEntity, memberEntity)
                    .orElse(null);

            FavoriteDto favoriteDto = oldFavEntity.toDto();
            favoriteDto.setLatitude(latitude);
            favoriteDto.setLongitude(longitude);
            favoriteDto.setPlaceName(placeName);
            favoriteEntity = favoriteDto.toEntity();
        } else { // 2-2의 경우
            favoriteEntity = FavoriteEntity.builder()
                    .placeName(placeName)
                    .latitude(latitude)
                    .longitude(longitude)
                    .build();
        }
        favoriteEntity.setBbs(bbsEntity);
        favoriteEntity.setMember(memberEntity);

        favoriteRepository.save(favoriteEntity);
        return "OK";
    }

    public String cancelLocation(Long bid, Long memberId) {
        BbsEntity bbsEntity = bbsRepository.findById(bid).orElse(null);
        MemberEntity memberEntity = memberRepository.findById(memberId).orElse(null);


        FavoriteEntity favEntity = favoriteRepository.findByBbsAndMember(bbsEntity, memberEntity).orElse(null);

        if (favEntity != null) {
            favoriteRepository.deleteById(favEntity.getId());
            return "OK";
        } else {
            return "FAIL";
        }
    }

    public boolean findFavObject(Long bid, Long memberId) {
        return favoriteRepository.existsByBbsAndMember(bbsRepository.findById(bid).orElse(null), memberRepository.findById(memberId).orElse(null));
    }
}
