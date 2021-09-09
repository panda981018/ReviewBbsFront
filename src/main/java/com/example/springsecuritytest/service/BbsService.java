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

    public List<BbsDto> getBbsList(Long categoryId) { // 카테고리에 맞는 게시글 가져옴

        Optional<CategoryEntity> categoryEntity = categoryRepository.findById(categoryId);
        Optional<List<BbsEntity>> bbsEntities = bbsRepository.findByCategoryId(categoryEntity.get());
        List<BbsDto> bbsDtoList = new ArrayList<>();

        if (bbsEntities.isPresent()) {
            List<BbsEntity> bbsList = bbsEntities.get();

            for (BbsEntity bbs : bbsList) {
                bbsDtoList.add(bbs.toDto());
            }
        }
        return bbsDtoList;
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

    public void deleteEditorImg(String src) { // summernote editor에서 이미지 삭제
        FileUtils.deleteQuietly(new File(src));
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
}
