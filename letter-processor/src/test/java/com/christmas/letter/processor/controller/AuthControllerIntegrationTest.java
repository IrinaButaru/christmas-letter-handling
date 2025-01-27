package com.christmas.letter.processor.controller;

import com.christmas.letter.model.AuthRequest;
import com.christmas.letter.processor.LocalStackTestContainer;
import com.christmas.letter.processor.helper.UserTestHelper;
import com.christmas.letter.repository.UserRepository;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.shaded.com.fasterxml.jackson.core.JsonProcessingException;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("classpath:application.yml")
public class AuthControllerIntegrationTest extends LocalStackTestContainer {

    @Autowired
    private MockMvc mockMvc;

    @BeforeAll
    public static void initData(@Autowired UserRepository userRepository) {
        userRepository.saveAll(UserTestHelper.getUserEntities());
    }


    @AfterAll
    public static void cleanData(@Autowired UserRepository userRepository) {
        userRepository.deleteAll();
    }

    @Test
    @WithMockUser
    public void authenticateUser_WhenBadRequest_ShouldReturnErrorResponse() throws Exception {
        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getAuthBadRequest()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.code").value("failed.request.body.validation"))
                .andExpect(jsonPath("$.reasons").isArray())
                .andExpect(jsonPath("$.reasons", hasItem("email.must.be.valid")));
    }

    @Test
    @WithMockUser
    public void authenticateUser_WhenOK_ShouldReturnAuthResponse() throws Exception {
        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getAuthRequest()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.token").isNotEmpty())
                .andExpect(jsonPath("$.roles").isArray())
                .andExpect(jsonPath("$.roles").isNotEmpty());
    }

    private String getAuthRequest() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        return mapper.writeValueAsString(AuthRequest.builder()
                .email("santa@email.com")
                .password("test123!")
                .build());
    }

    private String getAuthBadRequest() throws JsonProcessingException {
       ObjectMapper mapper = new ObjectMapper();

        return mapper.writeValueAsString(AuthRequest.builder()
                .email("invalid")
                .password("test123!")
                .build());
    }


}
