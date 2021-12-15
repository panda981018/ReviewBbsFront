package com.example.springsecuritytest.service;

import com.example.springsecuritytest.domain.bbs.BbsEntity;
import com.example.springsecuritytest.domain.category.CategoryEntity;
import com.example.springsecuritytest.domain.heart.HeartEntity;
import com.example.springsecuritytest.domain.map.MapEntity;
import com.example.springsecuritytest.domain.member.MemberEntity;
import com.example.springsecuritytest.domain.reply.ReplyEntity;
import com.example.springsecuritytest.domain.category.CategoryRepository;
import com.example.springsecuritytest.domain.heart.HeartRepository;
import com.example.springsecuritytest.domain.map.MapRepository;
import com.example.springsecuritytest.domain.bbs.BbsQueryRepository;
import com.example.springsecuritytest.domain.bbs.BbsRepository;
import com.example.springsecuritytest.domain.member.MemberRepository;
import com.example.springsecuritytest.dto.BbsDto;
import com.example.springsecuritytest.dto.MemberDto;
import com.querydsl.core.Tuple;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.example.springsecuritytest.conf.AppConfig.localDateTimeToString;

@Slf4j
@Service
@AllArgsConstructor
public class BbsService {
    private final ImageService imageService;
    private final BbsRepository bbsRepository;
    private final BbsQueryRepository bbsQueryRepository;
    private final CategoryRepository categoryRepository;
    private final MapRepository mapRepository;
    private final MemberRepository memberRepository;
    private final HeartRepository heartRepository;

    // 게시물 저장
    @Transactional
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
        HashMap<String, Object> dataMap = new HashMap<>();

        BbsEntity bbsEntity = bbsRepository.findById(bbsId)
                .orElseThrow(() -> new Exception("POST NOT EXIST"));
        BbsDto bbsDto = bbsEntity.toDto();

        dataMap.put("bbsDto", bbsDto);
        List<ReplyEntity> replies = bbsEntity.getReplies();
        dataMap.put("replies", replies);

        return dataMap;
    }

    public void batchTest(LocalDateTime startDate, LocalDateTime endDate) {
        List<Tuple> countCategory = bbsQueryRepository.groupByCategory(startDate, endDate);
        for (int i = 0; i < countCategory.size(); i++) {
            log.info("CategoryName: " + countCategory.get(i).get(0, String.class));
            log.info(countCategory.get(i).get(0, String.class) + " count : " + countCategory.get(i).get(1, Integer.class));
        }
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
    @Transactional
    public void updateBbs(BbsDto bbsDto, MemberDto memberDto) throws Exception {
        double latitude = bbsDto.getLatitude();
        double longitude = bbsDto.getLongitude();

        BbsEntity oldBbs = bbsRepository.findById(bbsDto.getId())
                .orElseThrow(() -> new Exception("MEMBER IS NOT EXISTS"));
        CategoryEntity category = categoryRepository.findById(bbsDto.getCategoryId())
                .orElseThrow(() -> new Exception("CATEGORY NOT EXIST"));

        bbsDto.setBbsDate(localDateTimeToString(oldBbs.getBbsDate())); // set date
        bbsDto.setBbsViews(oldBbs.getBbsViews()); // set bbsView
        bbsDto.setLikeCnt(oldBbs.getLikeCnt());

        BbsEntity bbs = bbsDto.toEntity();

        bbs.setCategory(category);
        bbs.setBbsWriter(memberDto.toEntity()); // set writer

        if (oldBbs.getMap() != null) {
            if (oldBbs.getMap().getLatitude() == latitude && oldBbs.getMap().getLongitude() == longitude) {
                bbs.setMap(oldBbs.getMap());
                bbsRepository.save(bbs);
            } else {
                MapEntity map = MapEntity.builder()
                        .latitude(latitude)
                        .longitude(longitude)
                        .placeName(bbsDto.getPlaceName())
                        .build();
                mapRepository.save(map);
                bbs.setMap(map);
                bbsRepository.save(bbs);
                map.getBbsEntityList().add(bbs);
            }
        } else { // bbs에 map이 없으면
            MapEntity map;
            if (mapRepository.existsByLatitudeAndLongitude(latitude, longitude)) {
                map = mapRepository.findByLatitudeAndLongitude(latitude, longitude).orElseThrow(null);
                bbs.setMap(map);
                bbsRepository.save(bbs);
                map.getBbsEntityList().add(bbs);
                mapRepository.save(map);
            } else {
                map = MapEntity.builder()
                        .latitude(latitude)
                        .longitude(longitude)
                        .placeName(bbsDto.getPlaceName())
                        .build();
                bbs.setMap(map);
                bbsRepository.save(bbs);
                map.getBbsEntityList().add(bbs);
                mapRepository.save(map);
            }
        }
    }

    // 조회수 업데이트
    @Transactional
    public void updateViews(Long bid) throws Exception {
        BbsEntity bbsEntity = bbsRepository.findById(bid)
                .orElseThrow(() -> new Exception("POST NOT EXIST"));
        bbsQueryRepository.updateBbsViews(bid, bbsEntity.getBbsViews());
    }

    @Transactional
    public void deleteBbs(Long id, List<String> urls) { // 게시글 삭제
        BbsEntity bbsEntity = bbsRepository.findById(id).orElse(null);
        List<HeartEntity> heartEntitiesAboutBbs = heartRepository.findByBbs(bbsEntity);

        if (heartEntitiesAboutBbs.size() != 0) { // bbs와 관련된 객체가 있는지 찾아서 있으면 삭제
            for (HeartEntity heart : heartEntitiesAboutBbs) {
                heartRepository.deleteById(heart.getId());
            }
        }

        if (urls != null) { // 이미지가 있다면 사진 저장소에서 이미지 삭제
            imageService.deleteUploadedImg(urls);
        }

        if (bbsEntity.getMap() == null) { // 게시물에서 저장한 위치가 null이라면 bbs 그냥 삭제
            bbsRepository.deleteById(id);
        } else { // 게시물에서 저장한 위치가 null이 아니라면 map객체에서 게시글 삭제하고 게시물 삭제
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
    @Transactional
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
