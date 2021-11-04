package com.example.springsecuritytest.service;

import com.example.springsecuritytest.domain.entity.*;
import com.example.springsecuritytest.domain.repository.CategoryRepository;
import com.example.springsecuritytest.domain.repository.MapRepository;
import com.example.springsecuritytest.domain.repository.bbs.BbsQueryRepository;
import com.example.springsecuritytest.domain.repository.bbs.BbsRepository;
import com.example.springsecuritytest.domain.repository.member.MemberRepository;
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

@Service
@AllArgsConstructor
public class BbsService {
    private final ImageService imageService;
    private final BbsRepository bbsRepository;
    private final BbsQueryRepository bbsQueryRepository;
    private final CategoryRepository categoryRepository;
    private final MapRepository mapRepository;
    private final MemberRepository memberRepository;

    // 게시물 저장
    public void saveBbs(BbsDto bbsDto, MemberDto memberDto) throws Exception {
        double latitude = bbsDto.getLatitude();
        double longitude = bbsDto.getLongitude();

        CategoryEntity category = categoryRepository.findById(bbsDto.getCategoryId())
                .orElseThrow(() -> new Exception("CATEGORY NOT EXIST"));

        LocalDateTime now = LocalDateTime.now();
        String time = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        bbsDto.setBbsDate(time);
        bbsDto.setBbsViews(0);
        bbsDto.setLikeCnt(0);

        MapEntity mapEntity;

        BbsEntity bbs = bbsDto.toEntity();
        CategoryEntity categoryEntity = category;
        bbs.setCategory(categoryEntity);
        bbs.setBbsWriter(memberDto.toEntity());

        if (mapRepository.existsByLatitudeAndLongitude(latitude, longitude)) {
            mapEntity = mapRepository.findByLatitudeAndLongitude(latitude, longitude).orElse(null);
            bbs.setMap(mapEntity);
            bbsRepository.save(bbs);
            mapEntity.addBbs(bbs);
            mapRepository.save(mapEntity);
        } else {
            mapEntity = MapEntity.builder()
                    .latitude(bbsDto.getLatitude())
                    .longitude(bbsDto.getLongitude())
                    .placeName(bbsDto.getPlaceName())
                    .build();
            mapRepository.save(mapEntity);
            bbs.setMap(mapEntity);
            mapEntity.addBbs(bbs);
            bbsRepository.save(bbs);
        }
    }

    // 게시물 1개
    public HashMap<String, Object> getBbs(Long bbsId) throws Exception {

        BbsEntity bbsEntity = bbsRepository.findById(bbsId)
                .orElseThrow(() -> new Exception("POST NOT EXIST"));
        BbsDto bbsDto;
        HashMap<String, Object> dataMap = new HashMap<>();

        bbsDto = bbsEntity.toDto();
        dataMap.put("bbsDto", bbsDto);
        List<ReplyEntity> replies = bbsEntity.getReplies();
        dataMap.put("replies", replies);

        return dataMap;
    }

    // 카테고리 상관없이 찾기
    public List<BbsDto> findAll(Pageable pageable) {
        Page<BbsEntity> paging = bbsRepository.findAll(pageable);
        List<BbsEntity> bbsEntities = paging.getContent();
        List<BbsDto> bbsDtoList = new ArrayList<>();
        for (BbsEntity bbs : bbsEntities) {
            bbsDtoList.add(bbs.toDto());
        }
        return bbsDtoList;
    }

    public HashMap<String, Object> getBbsPagination(Long categoryId, Pageable pageable, String searchType, String keyword)
            throws Exception { // 카테고리 내에서 column을 기준으로 내림차순 정렬
        Page<BbsEntity> bbsEntities = null;
        List<BbsEntity> entities;
        List<BbsDto> bbsDtoList = new ArrayList<>();
        long totalCount = 0L;

        if (searchType == null) {
            CategoryEntity category = categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new Exception("CATEGORY NOT EXIST"));
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

    private Page<BbsEntity> findByBbsTitle(Long categoryId, Pageable pageable, String keyword) throws Exception {
        Page<BbsEntity> bbsEntities = null;

        CategoryEntity categoryEntity = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new Exception("CATEGORY NOT EXIST"));
        bbsEntities = bbsRepository.findByCategoryIdAndBbsTitleContainingIgnoreCase(pageable, categoryEntity, keyword);

        return bbsEntities;
    }

    public Page<BbsEntity> findByWriter(Long categoryId, Pageable pageable, String writer) throws Exception { // 작성자를 찾을 때
        MemberEntity memberEntity = memberRepository.findByNickname(writer)
                .orElse(null);
        CategoryEntity categoryEntity = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new Exception("CATEGORY NOT EXIST"));
        Page<BbsEntity> bbsEntities;

        CategoryEntity category = categoryEntity;
        if (memberEntity != null)
            bbsEntities = bbsRepository.findByCategoryIdAndBbsWriter(pageable, category, memberEntity);
        else { // exception
            bbsEntities = null;
        }
        return bbsEntities;
    }

    // 게시글 수정
    public void updateBbs(BbsDto bbsDto, MemberDto memberDto) throws Exception {
        CategoryEntity category = categoryRepository.findById(bbsDto.getCategoryId())
                .orElseThrow(() -> new Exception("CATEGORY NOT EXIST"));
        BbsEntity bbsEntity = bbsRepository.findById(bbsDto.getId())
                .orElseThrow(() -> new Exception("MEMBER IS NOT EXISTS"));

        BbsEntity oldBbs = bbsEntity; // bbsdate, writer, bbsView 가져오기
        bbsDto.setBbsDate(oldBbs.getBbsDate()); // set date
        bbsDto.setBbsViews(oldBbs.getBbsViews()); // set bbsView
        bbsDto.setLikeCnt(oldBbs.getLikeCnt());

        BbsEntity bbs = bbsDto.toEntity();
        CategoryEntity categoryEntity = category; // set category
        bbs.setCategory(categoryEntity);
        bbs.setBbsWriter(memberDto.toEntity()); // set writer
        bbsRepository.save(bbs);
    }

    // 조회수 업데이트
    public void updateViews(Long bid) throws Exception {
        BbsEntity bbsEntity = bbsRepository.findById(bid)
                .orElseThrow(() -> new Exception("POST NOT EXIST"));
        bbsQueryRepository.updateBbsViews(bid, bbsEntity.getBbsViews());
    }

    public void deleteBbs(Long id, List<String> urls) { // 게시글 삭제
        if (urls != null) {
            imageService.deleteUploadedImg(urls);
        }
        BbsEntity bbsEntity = bbsRepository.findById(id).orElse(null);
        if (bbsEntity.getMap() == null) {
            bbsRepository.deleteById(id);
        } else {
            MapEntity mapEntity = bbsEntity.getMap();

            if (mapEntity.getBbsEntityList().size() == 0) {
                mapRepository.deleteById(mapEntity.getId());
            } else {
                mapEntity.getBbsEntityList().remove(bbsEntity);
                mapRepository.save(mapEntity);
            }
            bbsRepository.deleteById(id);
        }
    }

    //  JPA saveAndFlush를 사용한 방법
    public int updateLikeCount(Long bid, String type) throws Exception {
        BbsEntity optionalBbsEntity = bbsRepository.findById(bid).orElseThrow(() -> new Exception("POST NOT EXIST"));

        BbsEntity bbsEntity = optionalBbsEntity;
        BbsDto oldBbsDto = bbsEntity.toDto();
        MemberEntity member = memberRepository.findByNickname(oldBbsDto.getBbsWriter())
                .orElseThrow(() -> new Exception("MEMBER NOT EXIST"));
        CategoryEntity category = categoryRepository.findById(oldBbsDto.getCategoryId())
                .orElseThrow(() -> new Exception("CATEGORY NOT EXIST"));

        if (type.equals("like")) {
            oldBbsDto.setLikeCnt(bbsEntity.getLikeCnt() + 1);
        } else {
            oldBbsDto.setLikeCnt(bbsEntity.getLikeCnt() - 1);
        }
        BbsEntity newBbsEntity = oldBbsDto.toEntity();
        newBbsEntity.setCategory(category);
        newBbsEntity.setBbsWriter(member);
        bbsRepository.saveAndFlush(newBbsEntity);

        BbsEntity newBbs = bbsRepository.findById(bid).orElseThrow(() -> new Exception("POST NOT EXIST"));
        int likeCnt = newBbs.getLikeCnt();
        return likeCnt;
    }
}
