package com.example.springsecuritytest.service;

import com.example.springsecuritytest.domain.Role;
import com.example.springsecuritytest.domain.entity.MemberEntity;
import com.example.springsecuritytest.domain.repository.MemberRepository;
import com.example.springsecuritytest.dto.MemberDto;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class MemberService implements UserDetailsService {

    private MemberRepository memberRepository;

    @Transactional
    public Long joinUser(MemberDto memberDto) {
        // 비밀번호 암호화
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        memberDto.setPassword(passwordEncoder.encode(memberDto.getPassword()));

        return memberRepository.save(memberDto.toEntity()).getId();
    }

    @Override
    // 상세 정보를 조회하는 메서드. 계정정보와 권한을 갖는 UserDetails 인터페이스를 return 해야함.
    // 매개변수는 로그인 시 입력한 아이디. Entity의 PK를 뜻하는게 아니라 유저를 식별할 수 있는 어떤 값을 의미. Security에선 username이라는 이름으로 사용됨.
    // 여기서는 아이디가 이메일 형식, 로그인하는 form에서 name="username"으로 요청해야함.
    public UserDetails loadUserByUsername(String userEmail) throws UsernameNotFoundException {
        Optional<MemberEntity> userEntityWrapper = memberRepository.findByEmail(userEmail);
        MemberEntity userEntity = userEntityWrapper.get();

        List<GrantedAuthority> authorities = new ArrayList<>();

        // authorities.add(new SimpleGrantedAuthority())는 롤을 부여하는 코드.
        // 회원가입할 때 Role을 정할 수 있도록 Role Entity를 만들어서 매핑해주는 것이 좋은 방법.
        // 예제에서는 admin@example.com이 관리자 계정이라고 설정함.
        if (("admin@example.com").equals(userEmail)) {
            authorities.add(new SimpleGrantedAuthority(Role.ADMIN.getValue()));
        } else {
            authorities.add(new SimpleGrantedAuthority(Role.MEMBER.getValue()));
        }

        // User는 spring security가 제공하는 User 패키지를 사용할 것.
        // return new User(아이디, 비밀번호, 권한 리스트);
        return new User(userEntity.getEmail(), userEntity.getPassword(), authorities);
    }

}
