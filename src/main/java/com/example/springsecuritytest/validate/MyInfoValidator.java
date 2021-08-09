package com.example.springsecuritytest.validate;

import com.example.springsecuritytest.domain.repository.MemberQueryRepository;
import com.example.springsecuritytest.domain.repository.MemberRepository;
import com.example.springsecuritytest.dto.MemberDto;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.sql.SQLException;

@Component
@RequiredArgsConstructor
public class MyInfoValidator implements Validator {

    private final MemberQueryRepository memberQueryRepository;

    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.isAssignableFrom(MemberDto.class);
    }

    @SneakyThrows
    @Override
    public void validate(Object object, Errors errors) {
        MemberDto memberDto = (MemberDto) object;

        if (memberQueryRepository.findByNickname(memberDto.getId()).getResults().contains(memberDto.getNickname())) {
            System.out.println(memberQueryRepository.findByNickname(memberDto.getId()).getResults());
            errors.rejectValue("nickname", "invalid nickname", "이미 존재하는 닉네임입니다.");
        }
    }
}
