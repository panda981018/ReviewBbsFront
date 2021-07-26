package com.example.springsecuritytest.domain.entity;

import lombok.*;

import javax.persistence.*;
@SequenceGenerator(
        name = "USER_SEQ_GEN",
        sequenceName = "USER_SEQ",
        allocationSize = 1
)
@NoArgsConstructor(access = AccessLevel.PROTECTED) // 무분별한 Entity 생성을 막기 위해 protected
@Getter
@Entity
@Table(name = "MEMBER")
public class MemberEntity {
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "USER_SEQ_GEN")
    private Long id;

    @Column(length = 20, nullable = false)
    private String username;

    @Column(length = 100, nullable = false)
    private String password;

    @Column(length = 10, nullable = false)
    private String role;
    private String nickname;
    private String gender;
    private int age;

    @Column
    private String regDate;

    @Builder
    public MemberEntity(Long id, String username, String password, String role,
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
