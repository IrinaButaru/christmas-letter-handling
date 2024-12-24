package com.christmas.letter.processor.controller;

import com.christmas.letter.processor.LocalStackTestContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

@AutoConfigureMockMvc
@SpringBootTest
@TestPropertySource("classpath:test-config.properties")
public class LetterProcessorControllerIntegrationTest extends LocalStackTestContainer {

    @Autowired
    private MockMvc mockMvc;


}
