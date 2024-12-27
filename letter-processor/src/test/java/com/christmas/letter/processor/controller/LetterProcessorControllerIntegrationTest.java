package com.christmas.letter.processor.controller;

import com.christmas.letter.exception.GlobalExceptionHandler;
import com.christmas.letter.model.LetterEntity;
import com.christmas.letter.model.mapper.DynamoDbLetterMapper;
import com.christmas.letter.processor.LocalStackTestContainer;
import com.christmas.letter.processor.helper.LetterTestHelper;
import com.christmas.letter.repository.LetterRepository;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("classpath:application.yml")
public class LetterProcessorControllerIntegrationTest extends LocalStackTestContainer {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private LetterRepository letterRepository;

    @BeforeAll
    public static void initData(@Autowired LetterRepository letterRepository) {
        letterRepository.saveAll(getLetterEntities());
    }

    @AfterAll
    public static void cleanData(@Autowired LetterRepository letterRepository) {
        letterRepository.deleteAll();
    }

    @Test
    public void getLetterByEmail_WhenOK_ShouldReturnLetter() throws Exception {
        String email = "ok@email.com";

        mockMvc.perform(get("/letter/{email}", email))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(email))
                .andExpect(jsonPath("$.name").value("Existing Name"))
                .andExpect(jsonPath("$.wishes").isArray())
                .andExpect(jsonPath("$.wishes", hasItem("gift")))
                .andExpect(jsonPath("$.wishes", hasItem("another gift")))
                .andExpect(jsonPath("$.deliveryAddress").value("test street no. 7"));

    }

    @Test
    public void getLetterByEmail_WhenInexistentEmail_ShouldReturnError() throws Exception {
        String email = "inexisting@email.com";

        mockMvc.perform(get("/letter/{email}", email))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.code").value(GlobalExceptionHandler.LETTER_NOT_FOUND_CODE))
                .andExpect(jsonPath("$.reasons").isArray())
                .andExpect(jsonPath("$.reasons", hasItem(String.format(GlobalExceptionHandler.LETTER_NOT_FOUND_MESSAGE, email))));

    }

    @Test
    public void getAllLetters_WhenOK_ShouldReturnPaginatedResponse() throws Exception {
        Pageable pageable = Pageable.ofSize(5).withPage(0);

        mockMvc.perform(get("/letter")
                        .param("page", String.valueOf(pageable.getPageNumber()))
                        .param("size", String.valueOf(pageable.getPageSize())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.letters").isArray())
                .andExpect(jsonPath("$.letters", hasSize(5)))
                .andExpect(jsonPath("$.totalPages").value(2))
                .andExpect(jsonPath("$.last").value(false));

        pageable = pageable.withPage(1);

        mockMvc.perform(get("/letter")
                        .param("page", String.valueOf(pageable.getPageNumber()))
                        .param("size", String.valueOf(pageable.getPageSize())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.letters").isArray())
                .andExpect(jsonPath("$.letters", hasSize(4)))
                .andExpect(jsonPath("$.totalPages").value(2))
                .andExpect(jsonPath("$.last").value(true));

    }

    @Test
    public void getAllLetters_WhenNoLetters_ShouldReturnZeroLetters() throws Exception {
        letterRepository.deleteAll();

        Pageable pageable = Pageable.ofSize(5).withPage(0);

        mockMvc.perform(get("/letter")
                        .param("page", String.valueOf(pageable.getPageNumber()))
                        .param("size", String.valueOf(pageable.getPageSize())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.letters").isArray())
                .andExpect(jsonPath("$.letters", hasSize(0)))
                .andExpect(jsonPath("$.totalPages").value(0))
                .andExpect(jsonPath("$.last").value(true));
    }

    public static List<LetterEntity> getLetterEntities() {
        return LetterTestHelper.createLetters(9)
                .stream()
                .map(DynamoDbLetterMapper.INSTANCE::objectToEntity)
                .toList();
    }

}
