package com.example.springsecuritytest.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class SignUpForm {
    // 길이 제한도 둘 예정.
    @NotBlank
    private String username;
    @NotBlank
    private String password;
    @NotBlank
    private String role;
    @NotBlank
    private String nickname;
    @NotBlank
    private String gender;
    private int age;
}
