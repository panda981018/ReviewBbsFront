package com.example.springsecuritytest.service;

import lombok.AllArgsConstructor;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class ImageService {

    public void deleteUploadedImg(List<String> urls) { // 게시글에 포함된 이미지 삭제
        for (String url : urls) {
            System.out.println("image urls : " + url);
            FileUtils.deleteQuietly(new File(url));
        }
    }

    public HashMap<String, String> uploadImage(List<MultipartFile> multipartFile) { // 이미지 업로드
        HashMap<String, String> data = new HashMap<>();

        String fileRoot = "C:\\summernoteImg\\"; // 저장될 경로

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
