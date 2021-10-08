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

    // BbsApiController.getAllBbsList에서 사용
    public HashMap<String, Object> findAll(Long categoryId, Pageable pageable, String searchType, String keyword) { // 카테고리 내에서 column을 기준으로 내림차순 정렬
        Page<BbsEntity> bbsEntities;
        List<BbsEntity> entities = new ArrayList<>();
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

        if (bbsEntities == null) {
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
            bbsEntities = bbsRepository.findByCategoryIdAndBbsTitleContainingIgnoreCase(pageable, optionalCategory.get(), keyword);
        }

        return bbsEntities;
    }

//    public HashMap<String, Object> findAll(Long categoryId, Pageable pageable, String searchType, String keyword) { // 카테고리 내에서 column을 기준으로 내림차순 정렬
//        CategoryEntity category = categoryRepository.findById(categoryId).get();
//        Page<BbsEntity> bbsEntities = bbsRepository.findByCategoryId(category, pageable);
//
//        List<BbsDto> bbsDtoList = new ArrayList<>();
//        List<BbsEntity> entities = bbsEntities.getContent();
//
//        for(BbsEntity bbsEntity : entities) {
//            bbsDtoList.add(bbsEntity.toDto());
//        }
//
//        HashMap<String, Object> dataMap = new HashMap<>();
//        dataMap.put("bbsDtoList", bbsDtoList);
//        dataMap.put("totalCount", bbsEntities.getTotalElements());
//
//        return dataMap;
//    }

    public Page<BbsEntity> findByWriter(Long category, Pageable pageable, String writer) { // 작성자를 찾을 때
        Optional<MemberEntity> optionalMember = memberRepository.findByNickname(writer);
        Optional<CategoryEntity> optionalCategory = categoryRepository.findById(category);
        Page<BbsEntity> bbsEntities = null;
        if (optionalMember.isPresent() && optionalCategory.isPresent()) {
            MemberEntity member = optionalMember.get();
            bbsEntities = bbsRepository.findByCategoryIdAndBbsWriter(pageable, member, optionalCategory.get());
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
}
