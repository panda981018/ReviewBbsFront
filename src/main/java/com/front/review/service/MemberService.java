package com.front.review.service;

import com.front.review.dto.MemberDto;
import com.front.review.enumclass.Role;
import com.front.review.handler.MemberRequestHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
@EnableWebSecurity
public class MemberService implements UserDetailsService {

    private final MemberRequestHandler memberRequestHandler;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        MemberDto memberDto = memberRequestHandler.getMemberDto(username);

        if (memberDto != null) {
            List<GrantedAuthority> authorities = new ArrayList<>();
            if (memberDto.getRole().equals(Role.ADMIN.getTitle())) { // admin role을 가지고 있다면
                authorities.add(new SimpleGrantedAuthority(Role.ADMIN.getValue()));
            } else { // member role을 가지고 있다면
                authorities.add(new SimpleGrantedAuthority(Role.MEMBER.getValue()));
            }

            return new User(memberDto.getUsername(), memberDto.getPassword(), authorities);
        } else {
            throw new UsernameNotFoundException("해당 이메일로 가입된 계정이 없습니다.");
        }
    }

    public Long signUp(MemberDto memberDto) { // Post, signup
        LocalDateTime now = LocalDateTime.now();
        String time = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String cryptoPassword = passwordEncoder.encode(memberDto.getPassword());

        memberDto.setPassword(cryptoPassword);
        memberDto.setRegDate(time);

        return memberRequestHandler.signUpRequest(memberDto);
    }

    public void updateMemberInfo(HttpSession session, MemberDto memberDto) { // POST, update
        if (memberDto.getPassword().length() != 0) { // 비번 바꿈.
            memberDto.setPassword(passwordEncoder.encode(memberDto.getPassword()));
        }
        memberRequestHandler.updateMyInfoRequest(session, memberDto);
    }

    public ResponseEntity<Boolean> checkEmail(HashMap<String, String> usernameObj) {
        return memberRequestHandler.checkEmail(usernameObj);
    }

    public ResponseEntity<Boolean> checkNickname(HashMap<String, String> nicknameObj) {
        return memberRequestHandler.checkNickname(nicknameObj);
    }
}
