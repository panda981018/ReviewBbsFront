package com.example.springsecuritytest.dto;

import com.example.springsecuritytest.domain.entity.MemberEntity;
import com.example.springsecuritytest.enumclass.Role;
import lombok.*;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

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

    private String year;
    private String month;
    private String day;

    private String regDate;

    public MemberEntity toEntity() {
        if (role.equals(Role.ADMIN.getValue())) { // admin
            return MemberEntity.builder()
                    .id(id)
                    .username(username)
                    .password(password)
                    .role(Role.ADMIN)
                    .nickname(nickname)
                    .gender(gender)
                    .age(0)
                    .birth("")
                    .regDate(regDate)
                    .build();
        } else { // member
            return MemberEntity.builder()
                    .id(id)
                    .username(username)
                    .password(password)
                    .role(Role.MEMBER)
                    .nickname(nickname)
                    .gender(gender)
                    .age(calcAge(year))
                    .birth(year + "-" + month + "-" + day)
                    .regDate(regDate)
                    .build();
        }
    }

    @Builder
    public MemberDto(Long id, String username, String password, String role,
                     String nickname, String gender, String year, String month, String day, String regDate) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
        this.nickname = nickname;
        this.gender = gender;
        this.month = month;
        this.year = year;
        this.day = day;
        this.regDate = regDate;
    }

    public int calcAge(String year) {
        int thisYear = LocalDate.now().getYear();
        int age = thisYear - Integer.parseInt(year) + 1;
        return age;
    }
}
