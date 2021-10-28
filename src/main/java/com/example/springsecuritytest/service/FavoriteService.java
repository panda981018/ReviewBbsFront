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
    public String saveLocation(Long bid, Long memberId, double latitude, double longitude, String placeName) {
        Optional<BbsEntity> optionalBbs = bbsRepository.findById(bid);
        Optional<MemberEntity> optionalMember = memberRepository.findById(memberId);

        BbsEntity bbsEntity = null;
        MemberEntity memberEntity = null;

        FavoriteEntity favoriteEntity = null;

        if (optionalBbs.isPresent() && optionalMember.isPresent()) {
            bbsEntity = optionalBbs.get();
            memberEntity = optionalMember.get();
        }

        if (favoriteRepository.existsByBbsAndMember(bbsEntity, memberEntity)) { // 2-1의 경우
            Optional<FavoriteEntity> oldFavEntity = favoriteRepository.findByBbsAndMember(bbsEntity, memberEntity);
            if (oldFavEntity.isPresent()) {
                FavoriteDto favoriteDto = oldFavEntity.get().toDto();
                favoriteDto.setLatitude(latitude);
                favoriteDto.setLongitude(longitude);
                favoriteDto.setPlaceName(placeName);
                favoriteEntity = favoriteDto.toEntity();
            }
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
        Optional<BbsEntity> optionalBbs = bbsRepository.findById(bid);
        Optional<MemberEntity> optionalMember = memberRepository.findById(memberId);

        BbsEntity bbsEntity = null;
        MemberEntity memberEntity = null;

        if (optionalBbs.isPresent() && optionalMember.isPresent()) {
            bbsEntity = optionalBbs.get();
            memberEntity = optionalMember.get();
        }

        Optional<FavoriteEntity> favEntity = favoriteRepository.findByBbsAndMember(bbsEntity, memberEntity);
        if (favEntity.isPresent()) {
            favoriteRepository.deleteById(favEntity.get().getId());
            return "OK";
        } else {
            return "FAIL";
        }
    }

    public boolean findFavObject(Long bid, Long memberId) {
        return favoriteRepository.existsByBbsAndMember(bbsRepository.findById(bid).get(), memberRepository.findById(memberId).get());
    }
}
