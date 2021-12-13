package com.example.springsecuritytest.controller.sign;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT) // @WebMvcTest는 @SpringBOOTtEST와 같이 사용할 수 없다.
@AutoConfigureMockMvc // @Service나 @Repository가 붙은 객체들도 모두 메모리에 올린다.
public class SignApiControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    // Java 객체를 JSON으로 serialization 하기 위해
    private ObjectMapper objectMapper;

    @Test
    public void overlapEmail() throws Exception {
        String testUsername = "aa@example.com";
        HashMap<String, String> usernameObj = new HashMap<>();
        usernameObj.put("username", testUsername);

        mvc.perform(post("/api/check/email")
                        .content(objectMapper.writeValueAsString(usernameObj))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result", is(Boolean.TRUE)))
                .andDo(print());
    }

    @Test
    public void overlapNickname() throws Exception {
        String nickname = "aaaa";
        HashMap<String, Object> map = new HashMap<>();
        // view = "signUp"
//        map.put("id", null);
//        map.put("nickname", nickname);
//        map.put("view", "signUp");

        // view = "myInfo"
        map.put("id", 2);
        map.put("nickname", nickname);
        map.put("view", "myInfo");

        mvc.perform(post("/api/check/nickname")
                        .content(objectMapper.writeValueAsString(map))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result", is(Boolean.FALSE)))
                .andDo(print());
    }
}
