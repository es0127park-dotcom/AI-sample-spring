package com.example.demo.user;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@Transactional // 각각의 테스트 함수가 종료될 때마다 트랜잭션을 롤백
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class UserApiControllerTest {

    @Autowired
    private MockMvc mvc;

    @Test
    public void usernameCheck_duplicate_test() throws Exception {
        // given
        String username = "ssar";

        // when
        mvc.perform(get("/api/users/username-check?username=" + username))
                // then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.body").value(true));
    }

    @Test
    public void usernameCheck_available_test() throws Exception {
        // given
        String username = "newuser";

        // when
        mvc.perform(get("/api/users/username-check?username=" + username))
                // then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.body").value(false));
    }
}
