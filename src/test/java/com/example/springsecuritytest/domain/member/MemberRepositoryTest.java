package com.example.springsecuritytest.domain.member;

import com.example.springsecuritytest.enumclass.Gender;
import com.example.springsecuritytest.enumclass.Role;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
public class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    // 테스트용 멤버 데이터의 id들
    List<Long> testIds = new ArrayList<>();

    @After
    public void cleanUpTest() { // 테스트용 데이터 삭제
        for (Long id : testIds) {
            memberRepository.deleteById(id);
        }
    }

    @Test
    public void insertMember() {
        // username
        String username1 = "a@example.com";
        // password
        String password1 = passwordEncoder.encode("11111111");
        // role
        Role role1 = Role.MEMBER;
        // nickname
        String nickname1 = "a1234";
        // gender
        Gender gender1 = Gender.MALE;
        // age
        int age1 = 25;
        // birth
        Calendar birth1 = Calendar.getInstance();
        birth1.set(1997, 10, 25);
        // regDate
        LocalDateTime regDate1 = LocalDateTime.of(2021, 12, 2, 16, 37, 55);

        MemberEntity memberEntity1 = MemberEntity.builder()
                .username(username1)
                .password(password1)
                .role(role1)
                .nickname(nickname1)
                .gender(gender1)
                .age(age1)
                .birth(birth1)
                .regDate(regDate1)
                .build();
        memberRepository.save(memberEntity1);
        testIds.add(memberEntity1.getId());

        MemberEntity testEntity = memberRepository.findById(memberEntity1.getId()).get();

        assertThat(testEntity.getUsername()).isEqualTo(username1);
        assertThat(testEntity.getPassword()).isEqualTo(password1);
        assertThat(testEntity.getRole()).isEqualTo(Role.MEMBER);
        assertThat(testEntity.getNickname()).isEqualTo(nickname1);
        assertThat(testEntity.getGender()).isEqualTo(Gender.MALE);
        assertThat(testEntity.getAge()).isEqualTo(25);
        assertThat(testEntity.getRegDate()).isEqualTo(regDate1);
    }

    @Test
    public void updateMember() {

    }
}
