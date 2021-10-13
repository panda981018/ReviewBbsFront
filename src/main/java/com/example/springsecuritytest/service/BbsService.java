package com.example.springsecuritytest.service;

import com.example.springsecuritytest.domain.entity.BbsEntity;
import com.example.springsecuritytest.domain.entity.CategoryEntity;
import com.example.springsecuritytest.domain.entity.MemberEntity;
import com.example.springsecuritytest.domain.entity.ReplyEntity;
import com.example.springsecuritytest.domain.repository.BbsQueryRepository;
import com.example.springsecuritytest.domain.repository.BbsRepository;
import com.example.springsecuritytest.domain.repository.CategoryRepository;
import com.example.springsecuritytest.domain.repository.MemberRepository;
import com.example.springsecuritytest.dto.BbsDto;
import com.example.springsecuritytest.dto.MemberDto;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class BbsService {
    private final ImageService imageService;
    private final BbsRepository bbsRepository;
    private final BbsQueryRepository bbsQueryRepository;
    private final CategoryRepository categoryRepository;
    private final MemberRepository memberRepository;

    // 게시물 저장
    public void saveBbs(BbsDto bbsDto, MemberDto memberDto) {

        Optional<CategoryEntity> category = categoryRepository.findById(bbsDto.getCategoryId());

        if (category.isPresent()) {
            LocalDateTime now = LocalDateTime.now();
            String time = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

            bbsDto.setBbsDate(time);
            bbsDto.setBbsViews(0);
            bbsDto.setLikeCnt(0);

            BbsEntity bbs = bbsDto.toEntity();
            CategoryEntity categoryEntity = category.get();
            bbs.setCategory(categoryEntity);
            bbs.setBbsWriter(memberDto.toEntity());
            bbsRepository.save(bbs);
        }
    }

    // 게시물 1개
    public HashMap<String, Object> getBbs(Long bbsId) {

        Optional<BbsEntity> bbsEntity = bbsRepository.findById(bbsId);
        BbsDto bbsDto;
        HashMap<String, Object> dataMap = new HashMap<>();

        if (bbsEntity.isPresent()) {
            BbsEntity bbs = bbsEntity.get();
            bbsDto = bbs.toDto();
            dataMap.put("bbsDto", bbsDto);
            List<ReplyEntity> replies = bbs.getReplies();

            dataMap.put("replies", replies);
        }

        return dataMap;
    }

    public HashMap<String, Object> findAll(Long categoryId, Pageable pageable, String searchType, String keyword) { // 카테고리 내에서 column을 기준으로 내림차순 정렬
        Page<BbsEntity> bbsEntities;
        List<BbsEntity> entities;
        List<BbsDto> bbsDtoList = new ArrayList<>();
        long totalCount = 0L;

        if (searchType == null) {
            CategoryEntity category = categoryRepository.findById(categoryId).get();
            bbsEntities = bbsRepository.findByCategoryId(category, pageable);
        } else if (searchType.equals("bbsWriter")) { // writer
            bbsEntities = findByWriter(categoryId, pageable, keyword);
        } else { // title
            bbsEntities = findByBbsTitle(categoryId, pageable, keyword);
        }

        if (bbsEntities == null) { // 찾는 데이터가 없을 경우 null
            bbsDtoList.clear();
            totalCount = 0L;
        } else if (!bbsEntities.isEmpty()) {
            entities = bbsEntities.getContent();

            for (BbsEntity bbsEntity : entities) {
                bbsDtoList.add(bbsEntity.toDto());
            }

            totalCount = bbsEntities.getTotalElements();
        }

        HashMap<String, Object> dataMap = new HashMap<>();
        dataMap.put("bbsDtoList", bbsDtoList);
        dataMap.put("totalCount", totalCount);

        return dataMap;
    }

    private Page<BbsEntity> findByBbsTitle(Long category, Pageable pageable, String keyword) {
        Page<BbsEntity> bbsEntities = null;

        Optional<CategoryEntity> optionalCategory = categoryRepository.findById(category);
        if (optionalCategory.isPresent()) {
            bbsEntities
                    = bbsRepository.findByCategoryIdAndBbsTitleContainingIgnoreCase(pageable, optionalCategory.get(), keyword);
        }

        return bbsEntities;
    }

    public Page<BbsEntity> findByWriter(Long categoryId, Pageable pageable, String writer) { // 작성자를 찾을 때
        Optional<MemberEntity> optionalMember = memberRepository.findByNickname(writer);
        Optional<CategoryEntity> optionalCategory = categoryRepository.findById(categoryId);
        Page<BbsEntity> bbsEntities = null;
        if (optionalMember.isPresent() && optionalCategory.isPresent()) {
            MemberEntity member = optionalMember.get();
            CategoryEntity category = optionalCategory.get();
            bbsEntities = bbsRepository.findByCategoryIdAndBbsWriter(pageable, category, member);
        }
        return bbsEntities;
    }

    // 게시글 수정
    public void updateBbs(BbsDto bbsDto, MemberDto memberDto) {
        Optional<CategoryEntity> category = categoryRepository.findById(bbsDto.getCategoryId());
        Optional<BbsEntity> bbsEntity = bbsRepository.findById(bbsDto.getId());

        if (category.isPresent() && bbsEntity.isPresent()) {
            BbsEntity oldBbs = bbsEntity.get(); // bbsdate, writer, bbsView 가져오기
            bbsDto.setBbsDate(oldBbs.getBbsDate()); // set date
            bbsDto.setBbsViews(oldBbs.getBbsViews()); // set bbsView
            bbsDto.setLikeCnt(oldBbs.getLikeCnt());

            BbsEntity bbs = bbsDto.toEntity();
            CategoryEntity categoryEntity = category.get(); // set category
            bbs.setCategory(categoryEntity);
            bbs.setBbsWriter(memberDto.toEntity()); // set writer
            bbsRepository.save(bbs);
        }
    }

    // 조회수 업데이트
    public void updateViews(Long id) {
        BbsEntity bbsEntity = bbsRepository.findById(id).get();
        bbsQueryRepository.updateBbsViews(id, bbsEntity.getBbsViews());
    }

    public void deleteBbs(Long id, List<String> urls) { // 게시글 삭제
        if (urls != null) {
            imageService.deleteUploadedImg(urls);
        }
        bbsRepository.deleteById(id);
    }

//    [ 기존 방법 ]
//    public int updateLikeCount(Long bid, String type) {
//        Optional<BbsEntity> optionalBbsEntity = bbsRepository.findById(bid);
//        if (optionalBbsEntity.isPresent()) {
//            BbsEntity bbsEntity = optionalBbsEntity.get();
//            if (type.equals("like")) {
//                bbsQueryRepository.plusLikeCount(bid, bbsEntity.getLikeCnt());
//            } else {
//                bbsQueryRepository.minusLikeCount(bid, bbsEntity.getLikeCnt());
//            }
//            BbsEntity newBbsEntity = bbsRepository.findById(bid).get();
//            return newBbsEntity.getLikeCnt();
//        } else // 게시글이 존재하지 않는다면 -1 리턴
//            return -1;
//    }

//  querydsl 함수에서 임의로 +1 또는 -1 하는 값을 리턴하도록 만든 함수
//    public int updateLikeCount(Long bid, String type) {
//        Optional<BbsEntity> optionalBbsEntity = bbsRepository.findById(bid);
//        if (optionalBbsEntity.isPresent()) {
//            BbsEntity bbsEntity = optionalBbsEntity.get();
//            int likeCnt = 0;
//            if (type.equals("like")) {
//                likeCnt = bbsQueryRepository.plusLikeCount(bid, bbsEntity.getLikeCnt());
//            } else {
//                likeCnt = bbsQueryRepository.minusLikeCount(bid, bbsEntity.getLikeCnt());
//            }
//
//            return likeCnt;
//        } else return -1; // 게시글이 존재하지 않는다면 -1 리턴
//    }

    //  JPA saveAndFlush를 사용한 방법
    public int updateLikeCount(Long bid, Long memberId, String type) {
        Optional<BbsEntity> optionalBbsEntity = bbsRepository.findById(bid);

        if (optionalBbsEntity.isPresent()) {
            BbsEntity bbsEntity = optionalBbsEntity.get();
            BbsDto oldBbsDto = bbsEntity.toDto();
            CategoryEntity category = categoryRepository.findById(oldBbsDto.getCategoryId()).get();
            MemberEntity member = memberRepository.findById(memberId).get();
            if (type.equals("like")) {
                oldBbsDto.setLikeCnt(bbsEntity.getLikeCnt() + 1);
            } else {
                oldBbsDto.setLikeCnt(bbsEntity.getLikeCnt() - 1);
            }
            BbsEntity newBbsEntity = oldBbsDto.toEntity();
            newBbsEntity.setCategory(category);
            newBbsEntity.setBbsWriter(member);
            bbsRepository.saveAndFlush(newBbsEntity);

            int likeCnt = bbsRepository.findById(bid).get().getLikeCnt();
            return likeCnt;
        } else return -1; // 게시글이 존재하지 않는다면 -1 리턴
    }
}
