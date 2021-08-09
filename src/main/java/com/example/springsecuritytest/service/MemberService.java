package com.example.springsecuritytest.service;

import com.example.springsecuritytest.domain.entity.MemberEntity;
import com.example.springsecuritytest.domain.repository.MemberQueryRepository;
import com.example.springsecuritytest.domain.repository.MemberRepository;
import com.example.springsecuritytest.dto.MemberDto;
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

import javax.swing.text.html.Option;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class MemberService implements UserDetailsService {

    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;
    private final MemberQueryRepository memberQueryRepository;

    public void signUp(MemberDto memberDto) { // 회원가입

        LocalDateTime now = LocalDateTime.now();
        String time = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String cryptoPassword = passwordEncoder.encode(memberDto.getPassword());

        memberDto.setPassword(cryptoPassword);
        memberDto.setRegDate(time);

        memberRepository.save(memberDto.toEntity());
    }

    public MemberDto findByUsername(String username) throws SQLException { // 이름으로 회원정보 get

        Optional<MemberEntity> memberEntity = memberRepository.findByUsername(username);
        if(memberEntity.isPresent()) {
            return memberEntity.get().toDto();
        } else {
            throw new SQLException();
        }
    }

    public MemberDto findById(Long id) throws SQLException {
        Optional<MemberEntity> memberEntity = memberRepository.findById(id);
        if (memberEntity.isPresent()) {
            return memberEntity.get().toDto();
        } else {
            throw new SQLException();
        }
    }

    public void updateMember(MemberDto memberDto) { // 회원 정보 update

        Optional<MemberEntity> memberEntity = memberRepository.findByUsername(memberDto.getUsername());

        if (memberEntity.isPresent()) {
            MemberEntity member = memberEntity.get();

            if (memberDto.getPassword().isEmpty()) { // 비밀번호를 수정하지 않은 경우
                memberDto.setId(member.getId());
                memberDto.setPassword(member.getPassword());
            } else { // 비밀번호를 수정한 경우
                String cryptoPassword = passwordEncoder.encode(memberDto.getPassword());
                memberDto.setId(member.getId());
                memberDto.setPassword(cryptoPassword);
            }
            memberDto.setRole(member.getRole());
            memberDto.setGender(member.getGender());
            memberDto.setRegDate(member.getRegDate());

            memberRepository.save(memberDto.toEntity());
        }

    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<MemberEntity> memberEntity = memberRepository.findByUsername(username);

        if (memberEntity.isPresent()) {
            MemberEntity user = memberEntity.get();

            List<GrantedAuthority> authorities = new ArrayList<>();
            if (user.getRole().equals(Role.ADMIN.getValue())) { // admin role을 가지고 있다면
                authorities.add(new SimpleGrantedAuthority(Role.ADMIN.getValue()));
            } else { // member role을 가지고 있다면
                authorities.add(new SimpleGrantedAuthority(Role.MEMBER.getValue()));
            }

            return new User(user.getUsername(), user.getPassword(), authorities);
        } else {
            throw new UsernameNotFoundException(username);
        }
    }

    public List<MemberDto> findAllMembers() { // 모든 멤버들 리스트 출력

        List<MemberEntity> members = memberRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
        List<MemberDto> memberDtoList = new ArrayList<>();

        for (MemberEntity member : members) {
            MemberDto dto = member.toDto();
            memberDtoList.add(dto);
        }

        System.out.println(memberDtoList);
        return memberDtoList;
    }
}