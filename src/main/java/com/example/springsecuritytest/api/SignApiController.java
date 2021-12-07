package com.example.springsecuritytest.api;

import com.example.springsecuritytest.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.HashMap;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class SignApiController {

    private final MemberService memberService;

    @PostMapping("/check/email")
    public HashMap<String, Boolean> checkEmail(@RequestBody HashMap<String, String> usernameObj) {
        return memberService.checkEmail(usernameObj.get("username"));
    }

    @PostMapping("/check/nickname")
    public HashMap<String, Boolean> checkNickname(@RequestBody HashMap<String, String> nicknameObj) throws SQLException {
        return memberService.checkNickname(nicknameObj.get("id"), nicknameObj.get("nickname"), nicknameObj.get("view"));
    }
}
