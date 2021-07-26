package com.example.springsecuritytest.validate;

import com.example.springsecuritytest.domain.repository.MemberRepository;
import com.example.springsecuritytest.dto.SignUpForm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;


@Component
@RequiredArgsConstructor
public class SignUpFormValidator implements Validator {

    @Autowired
    private MemberRepository memberRepository;

    @Override
    public boolean supports(Class<?> aClass) {
        // 어떤 타입의 인스턴스를 검증할 것인가. 여기서는 SignUpForm
        return aClass.isAssignableFrom(SignUpForm.class);
    }

    @Override // 무엇을 검사할 것인가
    public void validate(Object object, Errors errors) {
        SignUpForm signUpForm = (SignUpForm) object;

        if (memberRepository.existsByUsername(signUpForm.getUsername())) {
            errors.rejectValue("username", "invalid username", "이미 사용 중인 아이디입니다.");
        }
        // password 길이 제한.
        if (signUpForm.getPassword().length() > 0 && signUpForm.getPassword().length() < 4) {
            errors.rejectValue("password", "password length is too short", "비밀번호의 길이가 너무 짧습니다.");
        }

        // nickname 이미 존재하면 error 발생시키기.
//        if (memberRepository.existsByNickname(signUpForm.getNickname())) {
//            errors.rejectValue("nickname", "nickname is already exist.", "이미 사용 중인 닉네임입니다.");
//        }

    }
}