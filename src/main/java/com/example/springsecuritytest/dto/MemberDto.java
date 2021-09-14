package com.example.springsecuritytest.dto;

import com.example.springsecuritytest.domain.entity.MemberEntity;
import com.example.springsecuritytest.enumclass.Gender;
import com.example.springsecuritytest.enumclass.Role;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Calendar;

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

    private String regDate;

    public MemberEntity toEntity() {
        if (role.equals(Role.ADMIN.getValue())) { // admin
            return MemberEntity.builder()
                    .id(id)
                    .username(username)
                    .password(password)
                    .role(Role.ADMIN)
                    .nickname(nickname)
                    .gender(null)
                    .age(0)
                    .birth(null)
                    .regDate(regDate)
                    .build();
        } else { // member
            Calendar birth = Calendar.getInstance();
            birth.set(Calendar.YEAR, Integer.parseInt(year));
            birth.set(Calendar.MONTH, Integer.parseInt(month)-1);
            birth.set(Calendar.DAY_OF_MONTH, Integer.parseInt(day));

            return MemberEntity.builder()
                    .id(id)
                    .username(username)
                    .password(password)
                    .role(Role.MEMBER)
                    .nickname(nickname)
                    .gender(gender.equals(Gender.MALE.getValue()) ? Gender.MALE : Gender.FEMALE)
                    .age(calcAge(year))
                    .birth(birth)
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
