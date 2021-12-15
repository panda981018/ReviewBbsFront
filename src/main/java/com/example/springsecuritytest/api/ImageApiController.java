package com.example.springsecuritytest.api;

import com.example.springsecuritytest.service.ImageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/summernote")
public class ImageApiController {

    private final ImageService imageService;

    @PostMapping("/deleteImg")
    public String deleteEditorImage(@RequestBody HashMap<String, List<String>> target) {

        log.info("Delete Image files : " + target.get("src"));
        imageService.deleteUploadedImg(target.get("src"));

        return "ok";
    }

    @PostMapping("/uploadImg")
    public HashMap<String, String> uploadSummernoteImage(@RequestParam("file") List<MultipartFile> multipartFile) {
        return imageService.uploadImage(multipartFile);
    }
}
