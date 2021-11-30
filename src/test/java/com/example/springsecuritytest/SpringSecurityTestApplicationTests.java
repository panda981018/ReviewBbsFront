package com.example.springsecuritytest;

import jdk.nashorn.internal.ir.annotations.Ignore;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.transaction.Transactional;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@Ignore
class SpringSecurityTestApplicationTests {

    @Test
    public void testStrings() {

    }

}
