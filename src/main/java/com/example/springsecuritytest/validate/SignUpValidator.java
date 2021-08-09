package com.example.springsecuritytest.validate;

import com.example.springsecuritytest.domain.repository.MemberQueryRepository;
import com.example.springsecuritytest.domain.repository.MemberRepository;
import com.example.springsecuritytest.dto.MemberDto;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class SignUpValidator implements Validator {

    private final MemberRepository memberRepository;
    private final MemberQueryRepository memberQueryRepository;

    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.isAssignableFrom(MemberDto.class);
    }

    @SneakyThrows
    @Override
    public void validate(Object object, Errors errors) {
        MemberDto memberDto = (MemberDto) object;
        System.out.println("[Validator] MemberDto: " + memberDto);

        if (memberRepository.existsByUsername(memberDto.getUsername())) {
            errors.rejectValue("username", "invalid username", "이미 사용 중인 아이디입니다.");
        }

        if (memberRepository.existsByNickname(memberDto.getNickname())) {
            errors.rejectValue("nickname", "invalid nickname", "이미 존재하는 닉네임입니다.");
        }
    }
}
