package com.example.springsecuritytest.service;

import com.example.springsecuritytest.domain.entity.BbsEntity;
import com.example.springsecuritytest.domain.entity.CategoryEntity;
import com.example.springsecuritytest.domain.repository.CategoryRepository;
import com.example.springsecuritytest.domain.repository.BbsRepository;
import com.example.springsecuritytest.domain.repository.BbsQueryRepository;
import com.example.springsecuritytest.dto.BbsDto;
import com.example.springsecuritytest.dto.MemberDto;
import lombok.AllArgsConstructor;
import org.apache.commons.io.FileUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@AllArgsConstructor
public class BbsService {

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

    public BbsDto getBbs(Long bbsId) { // 게시물 1개

        Optional<BbsEntity> bbsEntity = bbsRepository.findById(bbsId);
        BbsDto bbsDto = new BbsDto();
        if (bbsEntity.isPresent()) {
            BbsEntity bbs = bbsEntity.get();
            bbsDto = bbs.toDto();
        }
        return bbsDto;
    }

    public void updateBbs(BbsDto bbsDto, MemberDto memberDto) { // 게시글 수정
        Optional<CategoryEntity> category = categoryRepository.findById(bbsDto.getCategoryId());
        Optional<BbsEntity> bbsEntity = bbsRepository.findById(bbsDto.getId());

        if (category.isPresent()) {
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
            deleteUploadedImg(urls);
        }
        bbsRepository.deleteById(id);
    }

    public void deleteUploadedImg(List<String> urls) { // 게시글에 포함된 이미지 삭제
        for (String url: urls) {
            FileUtils.deleteQuietly(new File(url));
        }
    }

    public HashMap<String, String> uploadImage(List<MultipartFile> multipartFile) { // 이미지 업로드
        HashMap<String,String> data = new HashMap<>();

        String fileRoot = "D:\\summernoteImg\\"; // 저장될 경로

        for (int i = 0; i < multipartFile.size(); i++) {
            String originalFileName = multipartFile.get(i).getOriginalFilename();
            String type = originalFileName.substring(originalFileName.lastIndexOf("."));

            String savedFileName = UUID.randomUUID() + type;

            File targetFile = new File(fileRoot + savedFileName);

            try {
                InputStream fileStream = multipartFile.get(i).getInputStream();
                FileUtils.copyInputStreamToFile(fileStream, targetFile); // inputstream, 파일저장경로
                data.put("url", "/summernoteImg/" + savedFileName);
                data.put("responseCode", "success");
            } catch (IOException e) {
                FileUtils.deleteQuietly(targetFile); // 에러나면 저장된 파일 삭제
                data.put("responseCode", "error");
                e.printStackTrace();
            }
        }

        return data;
    }

    public Page<BbsEntity> findAllBbs(Pageable pageable, Long categoryId) {
        List<String> fields = new ArrayList<>();
        for(Sort.Order order : pageable.getSort()) {
            fields.add(order.getProperty());
        }

        Optional<CategoryEntity> categoryEntity = categoryRepository.findById(categoryId);
        Page<BbsEntity> bbsEntities = bbsQueryRepository.findAllCategoryBbs(categoryEntity.get(), pageable, fields);

        return bbsEntities;
    }
}
