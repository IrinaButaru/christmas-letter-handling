package com.christmas.letter.processor.controller;

import com.christmas.letter.exception.GlobalExceptionHandler;
import com.christmas.letter.processor.LocalStackTestContainer;
import com.christmas.letter.processor.helper.UserTestHelper;
import com.christmas.letter.processor.security.annotation.WithElfUser;
import com.christmas.letter.processor.security.annotation.WithGuestUser;
import com.christmas.letter.processor.security.annotation.WithSantaUser;
import com.christmas.letter.repository.LetterRepository;
import com.christmas.letter.repository.UserRepository;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

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

    @Autowired
    private UserRepository userRepository;

    @BeforeAll
    public static void initData(@Autowired LetterRepository letterRepository,
                                @Autowired UserRepository userRepository) {
        letterRepository.saveAll(UserTestHelper.getLetterEntities());
        userRepository.saveAll(UserTestHelper.getUserEntities());
    }


    @AfterAll
    public static void cleanData(@Autowired LetterRepository letterRepository,
                                 @Autowired UserRepository userRepository) {

        letterRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    public void getRequests_WhenNotAuthenticated_ShouldReturnError() throws Exception {
        String email = "ok@email.com";
        Pageable pageable = Pageable.ofSize(5).withPage(0);

        mockMvc.perform(get("/processor/letters/{email}", email))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code").value(GlobalExceptionHandler.UNAUTHORIZED_CODE))
                .andExpect(jsonPath("$.reasons").isArray())
                .andExpect(jsonPath("$.reasons", hasItem(GlobalExceptionHandler.UNAUTHORIZED_MESSAGE)));


        mockMvc.perform(get("/processor/letters")
                        .param("page", String.valueOf(pageable.getPageNumber()))
                        .param("size", String.valueOf(pageable.getPageSize())))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code").value(GlobalExceptionHandler.UNAUTHORIZED_CODE))
                .andExpect(jsonPath("$.reasons").isArray())
                .andExpect(jsonPath("$.reasons", hasItem(GlobalExceptionHandler.UNAUTHORIZED_MESSAGE)));
    }

    @Test
    @WithGuestUser
    public void getRequests_WhenUnauthorized_ShouldReturnError() throws Exception {
        String email = "ok@email.com";
        Pageable pageable = Pageable.ofSize(5).withPage(0);

        mockMvc.perform(get("/processor/letters/{email}", email))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.code").value(GlobalExceptionHandler.ACCESS_DENIED_CODE))
                .andExpect(jsonPath("$.reasons").isArray())
                .andExpect(jsonPath("$.reasons", hasItem(GlobalExceptionHandler.ACCESS_DENIED_MESSAGE)));

        mockMvc.perform(get("/processor/letters")
                        .param("page", String.valueOf(pageable.getPageNumber()))
                        .param("size", String.valueOf(pageable.getPageSize())))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.code").value(GlobalExceptionHandler.ACCESS_DENIED_CODE))
                .andExpect(jsonPath("$.reasons").isArray())
                .andExpect(jsonPath("$.reasons", hasItem(GlobalExceptionHandler.ACCESS_DENIED_MESSAGE)));
    }

    @Test
    @WithSantaUser
    public void getLetterByEmail_WhenSanta_ShouldReturnLetter() throws Exception {
        String email = "ok@email.com";

        mockMvc.perform(get("/processor/letters/{email}", email))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(email))
                .andExpect(jsonPath("$.name").value("Existing Name"))
                .andExpect(jsonPath("$.wishes").isArray())
                .andExpect(jsonPath("$.wishes", hasItem("gift")))
                .andExpect(jsonPath("$.wishes", hasItem("another gift")))
                .andExpect(jsonPath("$.deliveryAddress").value("test street no. 7"));

    }

    @Test
    @WithElfUser
    public void getLetterByEmail_WhenElf_ShouldReturnLetter() throws Exception {
        String email = "ok@email.com";

        mockMvc.perform(get("/processor/letters/{email}", email))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(email))
                .andExpect(jsonPath("$.name").value("Existing Name"))
                .andExpect(jsonPath("$.wishes").isArray())
                .andExpect(jsonPath("$.wishes", hasItem("gift")))
                .andExpect(jsonPath("$.wishes", hasItem("another gift")))
                .andExpect(jsonPath("$.deliveryAddress").value("test street no. 7"));

    }

    @Test
    @WithSantaUser
    public void getLetterByEmail_WhenInexistentEmail_ShouldReturnError() throws Exception {
        String email = "inexisting@email.com";

        mockMvc.perform(get("/processor/letters/{email}", email))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.code").value(GlobalExceptionHandler.LETTER_NOT_FOUND_CODE))
                .andExpect(jsonPath("$.reasons").isArray())
                .andExpect(jsonPath("$.reasons", hasItem(String.format(GlobalExceptionHandler.LETTER_NOT_FOUND_MESSAGE, email))));

    }

    @Test
    @WithSantaUser
    public void getAllLetters_WhenSanta_ShouldReturnPaginatedResponse() throws Exception {
        Pageable pageable = Pageable.ofSize(5).withPage(0);

        mockMvc.perform(get("/processor/letters")
                        .param("page", String.valueOf(pageable.getPageNumber()))
                        .param("size", String.valueOf(pageable.getPageSize())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.letters").isArray())
                .andExpect(jsonPath("$.letters", hasSize(5)))
                .andExpect(jsonPath("$.totalPages").value(2))
                .andExpect(jsonPath("$.last").value(false));

        pageable = pageable.withPage(1);

        mockMvc.perform(get("/processor/letters")
                        .param("page", String.valueOf(pageable.getPageNumber()))
                        .param("size", String.valueOf(pageable.getPageSize())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.letters").isArray())
                .andExpect(jsonPath("$.letters", hasSize(4)))
                .andExpect(jsonPath("$.totalPages").value(2))
                .andExpect(jsonPath("$.last").value(true));

    }

    @Test
    @WithElfUser
    public void getAllLetters_WhenElf_ShouldReturnPaginatedResponse() throws Exception {
        Pageable pageable = Pageable.ofSize(5).withPage(0);

        mockMvc.perform(get("/processor/letters")
                        .param("page", String.valueOf(pageable.getPageNumber()))
                        .param("size", String.valueOf(pageable.getPageSize())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.letters").isArray())
                .andExpect(jsonPath("$.letters", hasSize(5)))
                .andExpect(jsonPath("$.totalPages").value(2))
                .andExpect(jsonPath("$.last").value(false));

        pageable = pageable.withPage(1);

        mockMvc.perform(get("/processor/letters")
                        .param("page", String.valueOf(pageable.getPageNumber()))
                        .param("size", String.valueOf(pageable.getPageSize())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.letters").isArray())
                .andExpect(jsonPath("$.letters", hasSize(4)))
                .andExpect(jsonPath("$.totalPages").value(2))
                .andExpect(jsonPath("$.last").value(true));

    }

    @Test
    @WithSantaUser
    public void getAllLetters_WhenNoLetters_ShouldReturnZeroLetters() throws Exception {
        letterRepository.deleteAll();

        Pageable pageable = Pageable.ofSize(5).withPage(0);

        mockMvc.perform(get("/processor/letters")
                        .param("page", String.valueOf(pageable.getPageNumber()))
                        .param("size", String.valueOf(pageable.getPageSize())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.letters").isArray())
                .andExpect(jsonPath("$.letters", hasSize(0)))
                .andExpect(jsonPath("$.totalPages").value(0))
                .andExpect(jsonPath("$.last").value(true));
    }



}
