package com.example.springsecuritytest.controller;

import com.example.springsecuritytest.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/summernote")
public class ImageController {

    private final ImageService imageService;

    // img
    @ResponseBody
    @PostMapping("/deleteImg")
    public String deleteEditorImage(@RequestBody HashMap<String, List<String>> target) {

        System.out.println("Delete Image files : " + target.get("src"));
        imageService.deleteUploadedImg(target.get("src"));

        return "ok";
    }

    @ResponseBody
    @PostMapping("/uploadImg")
    public HashMap<String, String> uploadSummernoteImage(@RequestParam("file") List<MultipartFile> multipartFile) {
        return imageService.uploadImage(multipartFile);
    }
}
