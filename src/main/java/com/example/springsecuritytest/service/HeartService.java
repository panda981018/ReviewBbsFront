package com.example.springsecuritytest.service;

import com.example.springsecuritytest.domain.entity.BbsEntity;
import com.example.springsecuritytest.domain.entity.HeartEntity;
import com.example.springsecuritytest.domain.entity.MemberEntity;
import com.example.springsecuritytest.domain.repository.bbs.BbsRepository;
import com.example.springsecuritytest.domain.repository.HeartRepository;
import com.example.springsecuritytest.domain.repository.member.MemberRepository;
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
    public void plusHeartCount(Long bid, Long memberId) throws Exception {
        BbsEntity bbsEntity = bbsRepository.findById(bid)
                .orElse(null);
        MemberEntity memberEntity = memberRepository.findById(memberId)
                .orElse(null);
        HeartEntity heartEntity;

        if (heartRepository.existsByBbsAndMember(bbsEntity, memberEntity)) { // 2-1의 경우
            HeartEntity oldHeartEntity = heartRepository.findByBbsAndMember(bbsEntity, memberEntity).get();

            HeartDto heartDto = oldHeartEntity.toDto();
            heartDto.setLiked(true);
            heartEntity = heartDto.toEntity();

        } else { // 2-2의 경우
            heartEntity = HeartEntity.builder()
                    .isLiked(true)
                    .build();
        }
        heartEntity.setBbs(bbsEntity);
        heartEntity.setMember(memberEntity);

        heartRepository.save(heartEntity);
    }

    public void minusHeartCount(Long bid, Long memberId) throws Exception {
        BbsEntity bbsEntity = bbsRepository.findById(bid)
                .orElse(null);
        MemberEntity memberEntity = memberRepository.findById(memberId)
                .orElse(null);

        HeartEntity oldHeartEntity = heartRepository.findByBbsAndMember(bbsEntity, memberEntity)
                .orElse(null);


        HeartDto heartDto = oldHeartEntity.toDto();
        heartDto.setLiked(false);
        HeartEntity heartEntity = heartDto.toEntity();
        heartEntity.setBbs(bbsEntity);
        heartEntity.setMember(memberEntity);
        heartRepository.save(heartEntity);

    }

    public HeartDto findHeartObject(Long bid, Long memberId) throws Exception {
        BbsEntity bbsEntity = bbsRepository.findById(bid)
                .orElse(null);
        MemberEntity memberEntity = memberRepository.findById(memberId)
                .orElse(null);

        Optional<HeartEntity> optionalHeartEntity = heartRepository.findByBbsAndMember(bbsEntity, memberEntity);
        return optionalHeartEntity.map(HeartEntity::toDto).orElse(null);
    }
}
