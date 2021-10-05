package com.example.springsecuritytest.service;

import com.example.springsecuritytest.domain.entity.BbsEntity;
import com.example.springsecuritytest.domain.entity.CategoryEntity;
import com.example.springsecuritytest.domain.entity.ReplyEntity;
import com.example.springsecuritytest.domain.repository.BbsQueryRepository;
import com.example.springsecuritytest.domain.repository.BbsRepository;
import com.example.springsecuritytest.domain.repository.CategoryRepository;
import com.example.springsecuritytest.dto.BbsDto;
import com.example.springsecuritytest.dto.MemberDto;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.IOException;
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

    public HashMap<String, Object> getBbs(Long bbsId) { // 게시물 1개

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
    public List<BbsDto> findAll(Long categoryId, String column) {
        CategoryEntity category = categoryRepository.findById(categoryId).get();
        Optional<List<BbsEntity>> bbsEntities = null;
        if (column != null) {
            bbsEntities = bbsRepository.findByCategoryId(category, Sort.by(Sort.Direction.DESC, column));
        } else {
            bbsEntities = bbsRepository.findByCategoryId(category, Sort.by(Sort.Direction.DESC, "id"));
        }
        List<BbsDto> bbsDtoList = new ArrayList<>();
        List<BbsEntity> entities = bbsEntities.get();
        for(BbsEntity bbsEntity : entities) {
            bbsDtoList.add(bbsEntity.toDto());
        }
        return bbsDtoList;
    }

    public void updateBbs(BbsDto bbsDto, MemberDto memberDto) { // 게시글 수정
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

    public void updateViews(Long id) { // 조회수 업데이트
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
