package com.example.springsecuritytest.dto;

import com.example.springsecuritytest.domain.entity.MemberEntity;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class MemberDto {

    private Long id;
    private String email;
    private String password;
    private String createDate;

    public MemberEntity toEntity() {
        return MemberEntity.builder()
                .id(id)
                .email(email)
                .password(password)
                .createDate(createDate)
                .build();
    }

    @Builder
    public MemberDto(Long id, String email, String password, String createDate) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.createDate = createDate;
    }
}
