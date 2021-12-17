package com.front.review.dto;

import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class MemberDto implements Serializable {

    private Long id;
    private String username;
    private String password;
    private String role;
    private String nickname;
    private String gender;
    private String year;
    private String month;
    private String day;
    private String birth; // year + '-' + month + '-' + day
    private String regDate;

    @Builder // constructor
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
        this.birth = year + '-' + month + '-' + day;
        this.regDate = regDate;
    }

    public int calcAge(String year) {
        int thisYear = LocalDate.now().getYear();
        int age = thisYear - Integer.parseInt(year) + 1;
        return age;
    }
}
