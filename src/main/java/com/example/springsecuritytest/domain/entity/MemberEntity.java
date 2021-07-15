package com.example.springsecuritytest.domain.entity;

import com.sun.istack.NotNull;
import lombok.*;

import javax.persistence.*;

//name : generator 이름
// sequenceName : sequence 이름
// allocationSize : 할당할 크기
@SequenceGenerator(
        name = "USER_SEQ_GEN",
        sequenceName = "USER_SEQ",
        allocationSize = 1
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
@Entity
@Table(name = "MEMBER")
public class MemberEntity {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "USER_SEQ_GEN")
    private Long id;

    @Column(length = 20)
    @NotNull
    private String email;

    @Column(length = 100)
    @NotNull
    private String password;

    @Column
    private String createDate;

    @Builder
    public MemberEntity(Long id, String email, String password, String createDate) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.createDate = createDate;
    }
}
