package com.example.springsecuritytest.service;

import com.example.springsecuritytest.domain.entity.BbsEntity;
import com.example.springsecuritytest.domain.entity.HeartEntity;
import com.example.springsecuritytest.domain.entity.MemberEntity;
import com.example.springsecuritytest.domain.repository.BbsRepository;
import com.example.springsecuritytest.domain.repository.HeartRepository;
import com.example.springsecuritytest.domain.repository.MemberRepository;
import com.example.springsecuritytest.dto.HeartDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class HeartService {

    private final HeartRepository heartRepository;
    private final BbsRepository bbsRepository;
    private final MemberRepository memberRepository;

    // 1. 존재여부 확인
    // 2-1. 존재한다면 기존 객체의 isLiked를 true로 변경
    // 2-2. 존재하지 않는다면 새로운 HeartEntity를 생성하여 isLiked를 true로 설정
    // 3. 마지막에 save
    public void plusHeartCount(Long bid, Long memberId) {
        Optional<BbsEntity> optionalBbs = bbsRepository.findById(bid);
        Optional<MemberEntity> optionalMember = memberRepository.findById(memberId);

        BbsEntity bbsEntity = null;
        MemberEntity memberEntity = null;

        HeartEntity heartEntity = null;

        if (optionalBbs.isPresent() && optionalMember.isPresent()) {
            bbsEntity = optionalBbs.get();
            memberEntity = optionalMember.get();
        }

        if (heartRepository.existsByBbsAndMember(bbsEntity, memberEntity)) { // 2-1의 경우
            Optional<HeartEntity> oldHeartEntity = heartRepository.findByBbsAndMember(bbsEntity, memberEntity);
            if (oldHeartEntity.isPresent()) {
                HeartDto heartDto = oldHeartEntity.get().toDto();
                heartDto.setLiked(true);
                heartEntity = heartDto.toEntity();
            }
        } else { // 2-2의 경우
            heartEntity = HeartEntity.builder()
                    .isLiked(true)
                    .build();
        }
        heartEntity.setBbs(bbsEntity);
        heartEntity.setMember(memberEntity);

        heartRepository.save(heartEntity);
    }

    public void minusHeartCount(Long bid, Long memberId) {
        Optional<BbsEntity> optionalBbs = bbsRepository.findById(bid);
        Optional<MemberEntity> optionalMember = memberRepository.findById(memberId);

        BbsEntity bbsEntity = null;
        MemberEntity memberEntity = null;

        if (optionalBbs.isPresent() && optionalMember.isPresent()) {
            bbsEntity = optionalBbs.get();
            memberEntity = optionalMember.get();
        }

        Optional<HeartEntity> optionalHeartEntity = heartRepository.findByBbsAndMember(bbsEntity, memberEntity);
        if (optionalHeartEntity.isPresent()) {
            HeartDto heartDto = optionalHeartEntity.get().toDto();
            heartDto.setLiked(false);
            HeartEntity heartEntity = heartDto.toEntity();
            heartEntity.setBbs(bbsEntity);
            heartEntity.setMember(memberEntity);
            heartRepository.save(heartEntity);
        }
    }

    public HeartDto findHeartObject(Long bid, Long memberId) {
        Optional<BbsEntity> optionalBbs = bbsRepository.findById(bid);
        Optional<MemberEntity> optionalMember = memberRepository.findById(memberId);

        BbsEntity bbsEntity = null;
        MemberEntity memberEntity = null;

        if (optionalBbs.isPresent() && optionalMember.isPresent()) {
            bbsEntity = optionalBbs.get();
            memberEntity = optionalMember.get();
        }

        Optional<HeartEntity> optionalHeartEntity = heartRepository.findByBbsAndMember(bbsEntity, memberEntity);
        if (optionalHeartEntity.isPresent()) {
            return optionalHeartEntity.get().toDto();
        } else {
            return null;
        }
    }
}
