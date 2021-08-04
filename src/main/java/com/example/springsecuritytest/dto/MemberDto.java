package com.example.springsecuritytest.dto;

import com.example.springsecuritytest.domain.entity.MemberEntity;
import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class MemberDto {

    private Long id;
    @NotBlank
    private String username;
    @NotBlank
    private String password;
    private String role;
    @NotBlank
    private String nickname;
    private String gender;
    private int age;
    private String regDate;

    public MemberEntity toEntity() {
        return MemberEntity.builder()
                .id(id)
                .username(username)
                .password(password)
                .role(role)
                .nickname(nickname)
                .gender(gender)
                .age(age)
                .regDate(regDate)
                .build();
    }

    @Builder
    public MemberDto(Long id, String username, String password, String role,
                     String nickname, String gender, int age, String regDate) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
        this.nickname = nickname;
        this.gender = gender;
        this.age = age;
        this.regDate = regDate;
    }
}
