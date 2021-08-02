package com.example.springsecuritytest.service;

import com.example.springsecuritytest.domain.entity.MemberEntity;
import com.example.springsecuritytest.domain.repository.MemberQueryRepository;
import com.example.springsecuritytest.domain.repository.MemberRepository;
import com.example.springsecuritytest.dto.MemberDto;
import com.example.springsecuritytest.dto.SignUpForm;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class MemberService implements UserDetailsService {

    private PasswordEncoder passwordEncoder;
    private MemberRepository memberRepository;
    private MemberQueryRepository memberQueryRepository;

    public void signUp(SignUpForm signUpForm) {

        LocalDateTime now = LocalDateTime.now();
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

    public MemberDto findByUsername(String username) throws SQLException {

        Optional<MemberEntity> memberEntity = memberRepository.findByUsername(username);
        if(memberEntity.isPresent()) {
            MemberEntity user = memberEntity.get();
            MemberDto memberDto = MemberDto.builder()
                    .id(user.getId())
                    .username(user.getUsername())
                    .password(user.getPassword())
                    .role(user.getRole())
                    .gender(user.getGender())
                    .nickname(user.getNickname())
                    .age(user.getAge())
                    .regDate(user.getRegDate())
                    .build();
            return memberDto;
        } else {
            throw new SQLException();
        }
    }

    public void updateMember(MemberDto memberDto) throws SQLException {

        Optional<MemberEntity> memberData = memberRepository.findByUsername(memberDto.getUsername());

        if (memberData.isPresent()) {
            MemberDto afterMem;
            MemberEntity member = memberData.get();
            if (memberDto.getPassword().isEmpty()) {
                afterMem = MemberDto.builder()
                        .id(member.getId())
                        .username(memberDto.getUsername())
                        .password(memberData.get().getPassword())
                        .nickname(memberDto.getNickname())
                        .role(member.getRole())
                        .gender(member.getGender())
                        .age(memberDto.getAge())
                        .regDate(member.getRegDate())
                        .build();
            } else {
                afterMem = MemberDto.builder()
                        .id(member.getId())
                        .username(memberDto.getUsername())
                        .password(passwordEncoder.encode(memberDto.getPassword()))
                        .nickname(memberDto.getNickname())
                        .role(member.getRole())
                        .gender(member.getGender())
                        .age(member.getAge())
                        .regDate(member.getRegDate())
                        .build();
            }
            memberRepository.save(afterMem.toEntity());
        } else {
            throw new SQLException();
        }
        //memberQueryRepository.updateUserInfo(afterMem);

    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<MemberEntity> memberEntity = memberRepository.findByUsername(username);

        if (memberEntity.isPresent()) {
            MemberEntity user = memberEntity.get();

            List<GrantedAuthority> authorities = new ArrayList<>();
            if (user.getRole().equals("Admin")) { // admin role을 가지고 있다면
                authorities.add(new SimpleGrantedAuthority(Role.ADMIN.getValue()));
            } else { // member role을 가지고 있다면
                authorities.add(new SimpleGrantedAuthority(Role.MEMBER.getValue()));
            }

            return new User(user.getUsername(), user.getPassword(), authorities);
        } else {
            throw new UsernameNotFoundException(username);
        }
    }

    public List<MemberDto> findAllMembers() {

        List<MemberEntity> members = memberRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
        List<MemberDto> memberDtoList = new ArrayList<>();

        for (MemberEntity member : members) {
            MemberDto dto = MemberDto.builder()
                    .id(member.getId())
                    .username(member.getUsername())
                    .nickname(member.getNickname())
                    .gender(member.getGender())
                    .role(member.getRole())
                    .regDate(member.getRegDate())
                    .age(member.getAge())
                    .build();

            memberDtoList.add(dto);
        }
        return memberDtoList;
    }
}