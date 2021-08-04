package com.example.springsecuritytest.validate;

import com.example.springsecuritytest.domain.repository.MemberRepository;
import com.example.springsecuritytest.dto.MemberDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class MemberDtoValidator implements Validator {

    @Autowired
    private MemberRepository memberRepository;

    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.isAssignableFrom(MemberDto.class);
    }

    @Override
    public void validate(Object object, Errors errors) {
        MemberDto memberDto = (MemberDto) object;

        if (memberRepository.existsByUsername(memberDto.getUsername())) {
            errors.rejectValue("username", "invalid username", "이미 사용 중인 아이디입니다.");
        }
        if (memberRepository.countByNickname(memberDto.getNickname()) > 0) {
            int counting = memberRepository.countByNickname(memberDto.getNickname());
            System.out.println("[Validator] nickname : " + memberDto.getNickname() + "\ncount : " + counting);
            errors.rejectValue("nickname", "invalid nickname", "이미 사용 중인 닉네임입니다.");
        }
    }
}
