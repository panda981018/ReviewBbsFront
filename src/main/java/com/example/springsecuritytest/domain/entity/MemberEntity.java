package com.example.springsecuritytest.domain.entity;

import com.example.springsecuritytest.converter.RoleConverter;
import com.example.springsecuritytest.dto.MemberDto;
import com.example.springsecuritytest.enumclass.Gender;
import com.example.springsecuritytest.enumclass.Role;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor(access = AccessLevel.PROTECTED) // 무분별한 Entity 생성을 막기 위해 protected
@Getter
@Setter
@Entity
@Table(name = "JW_MEMBER")
public class MemberEntity {
    @Id
    @SequenceGenerator(
            name = "MEMBER_SEQ_GEN",
            sequenceName = "JW_MEMBER_SEQ",
            allocationSize = 1,
            initialValue = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "MEMBER_SEQ_GEN")
    private Long id;

    @Column(length = 30, nullable = false)
    private String username;

    @Column(length = 100, nullable = false)
    private String password;

    @Column(length = 20, nullable = false)
    @Convert(converter = RoleConverter.class)
    private Role role;

    @Column(length = 30, nullable = false)
    private String nickname;

    @Column(length = 10)
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(length = 4)
    @ColumnDefault("0")
    private int age;

    @Column(length = 15)
    @Temporal(value = TemporalType.DATE)
    private Calendar birth;

    @Column(length = 25)
    private LocalDateTime regDate;

    public MemberDto toDto() {
        String regDateStr = regDate.toString().replace("T", " ");
        if (role == Role.MEMBER) { // member
            return MemberDto.builder()
                    .id(id)
                    .username(username)
                    .password(password)
                    .nickname(nickname)
                    .role(Role.MEMBER.getTitle())
                    .gender(gender == Gender.MALE ? Gender.MALE.getValue() : Gender.FEMALE.getValue())
                    .year(Integer.toString(birth.get(Calendar.YEAR)))
                    .month(String.format("%02d", birth.get(Calendar.MONTH)+1))
                    .day(String.format("%02d", birth.get(Calendar.DAY_OF_MONTH)))
                    .regDate(regDateStr)
                    .build();
        } else { // admin
            return MemberDto.builder()
                    .id(id)
                    .username(username)
                    .nickname(nickname)
                    .gender(gender == Gender.MALE ? Gender.MALE.getValue() : Gender.FEMALE.getValue())
                    .role(Role.ADMIN.getTitle())
                    .regDate(regDateStr)
                    .build();
        }
    }


    @Builder
    public MemberEntity(Long id, String username, String password, Role role,
                        String nickname, Gender gender, int age, Calendar birth, LocalDateTime regDate) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
        this.nickname = nickname;
        this.gender = gender;
        this.age = age;
        this.birth = birth;
        this.regDate = regDate;
    }
}
