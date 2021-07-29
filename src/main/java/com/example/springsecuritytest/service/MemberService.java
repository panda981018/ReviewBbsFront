package com.example.springsecuritytest.service;

import com.example.springsecuritytest.domain.entity.MemberEntity;
import com.example.springsecuritytest.domain.repository.MemberQueryRepository;
import com.example.springsecuritytest.domain.repository.MemberRepository;
import com.example.springsecuritytest.dto.MemberDto;
import com.example.springsecuritytest.dto.SignUpForm;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class MemberService implements UserDetailsService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private MemberQueryRepository memberQueryRepository;

    @Transactional
    public void signUp(SignUpForm signUpForm) {

        LocalDateTime now = LocalDateTime.now();
        // HH = 24시간 포맷, hh = 12시간 포맷
        String time = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        MemberDto memberDto = MemberDto.builder()
                .username(signUpForm.getUsername())
                .password(passwordEncoder.encode(signUpForm.getPassword()))
                .nickname(signUpForm.getNickname())
                .gender(signUpForm.getGender())
                .age(signUpForm.getAge())
                .role(signUpForm.getRole())
                .regDate(time)
                .build();

        memberRepository.save(memberDto.toEntity());
    }

    public MemberDto findByUsername(String username) {

        MemberEntity memberEntity = memberRepository.findByUsername(username);
        MemberDto memberDto = MemberDto.builder()
                .username(memberEntity.getUsername())
                .password(memberEntity.getPassword())
                .nickname(memberEntity.getNickname())
                .age(memberEntity.getAge())
                .build();

        return memberDto;
    }

    public void updateMember(MemberDto memberDto) throws SQLException {
        MemberDto afterMem;

        if (memberDto.getPassword() == null) {
            afterMem = MemberDto.builder()
                    .username(memberDto.getUsername())
                    .nickname(memberDto.getNickname())
                    .age(memberDto.getAge())
                    .build();
        } else {
            afterMem = MemberDto.builder()
                    .username(memberDto.getUsername())
                    .password(passwordEncoder.encode(memberDto.getPassword()))
                    .nickname(memberDto.getNickname())
                    .age(memberDto.getAge())
                    .build();
        }

        memberQueryRepository.updateUserInfo(afterMem);

    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        MemberEntity user = memberRepository.findByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException(username);
        }

        List<GrantedAuthority> authorities = new ArrayList<>();

        if (user.getRole().equals("Admin")) { // admin role을 가지고 있다면
            authorities.add(new SimpleGrantedAuthority(Role.ADMIN.getValue()));
        } else { // member role을 가지고 있다면
            authorities.add(new SimpleGrantedAuthority(Role.MEMBER.getValue()));
        }

        return new User(user.getUsername(), user.getPassword(), authorities);
    }

}