package dev.orisha.jwt_demo.controllers;

import dev.orisha.jwt_demo.config.SecurityConfig;
import dev.orisha.jwt_demo.services.TokenService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest({HomeController.class, AuthController.class})
@Import({SecurityConfig.class, TokenService.class})
public class HomeControllerTest {

    @Autowired
    MockMvc mvc;

    @Test
    public void rootWhenUnauthenticatedThen401() throws Exception {
        mvc.perform(get("/"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void rootWhenAuthenticatedThenSaysHelloUser() throws Exception {
        MvcResult result = mvc.perform(post("/token")
                        .with(httpBasic("orisha", "password")))
                .andExpect(status().isOk())
                .andReturn();

        String token = result.getResponse().getContentAsString();

        mvc.perform(get("/")
                        .header("Authorization", "Bearer " + token))
                .andExpect(content().string("Hello, orisha"));
    }

    @Test
    @WithMockUser
    public void rootWithMockUserStatusIsOK() throws Exception {
        mvc.perform(get("/")).andExpect(status().isOk());
    }

}