package com.example.springsecuritytest.controller.favorite;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT) // @WebMvcTest는 @SpringBOOTtEST와 같이 사용할 수 없다.
@AutoConfigureMockMvc // @Service나 @Repository가 붙은 객체들도 모두 메모리에 올린다.
public class FavoriteApiControllerTest {

    @Test
    public void addFav() {

    }

    @Test
    public void cancleFav() {

    }

    @After
    public void deleteFav() {

    }
}
