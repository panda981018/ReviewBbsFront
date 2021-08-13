package com.example.springsecuritytest.domain.entity;

import com.example.springsecuritytest.converter.RoleConverter;
import com.example.springsecuritytest.dto.MemberDto;
import com.example.springsecuritytest.enumclass.Role;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
@SequenceGenerator(
        name = "USER_SEQ_GEN",
        sequenceName = "USER_SEQ",
        allocationSize = 1
)
@NoArgsConstructor(access = AccessLevel.PROTECTED) // 무분별한 Entity 생성을 막기 위해 protected
@Getter
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "MEMBER")
public class MemberEntity {
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "USER_SEQ_GEN")
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
    private String gender;

    @Column(length = 4)
    @ColumnDefault("0")
    private int age;

    @Column(length = 15)
    private String birth;

    @Column(length = 25)
    private String regDate;

    public MemberDto toDto() {
        if (role == Role.MEMBER) { // member
            String[] str = birth.split("-");

            return MemberDto.builder()
                    .id(id)
                    .username(username)
                    .password(password)
                    .nickname(nickname)
                    .role(Role.MEMBER.getValue())
                    .gender(gender)
                    .year(str[0])
                    .month(str[1])
                    .day(str[2])
                    .regDate(regDate)
                    .build();
        } else { // admin
            return MemberDto.builder()
                    .id(id)
                    .username(username)
                    .nickname(nickname)
                    .gender(gender)
                    .role(Role.ADMIN.getValue())
                    .regDate(regDate)
                    .build();
        }
    }


    @Builder
    public MemberEntity(Long id, String username, String password, Role role,
                        String nickname, String gender, int age, String birth, String regDate) {
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
