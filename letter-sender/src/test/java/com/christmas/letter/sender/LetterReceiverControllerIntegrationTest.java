package com.christmas.letter.sender;

import com.christmas.letter.sender.dto.ChristmasLetterRequest;
import com.christmas.letter.sender.service.LetterReceiverService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.shaded.com.fasterxml.jackson.core.JsonProcessingException;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
public class LetterReceiverControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

//    @MockBean
//    private LetterReceiverService letterReceiverService;

    @Test
    public void publishLetter_WhenBadRequest_ShouldReturnErrorResponse() throws Exception {
        mockMvc.perform(post("/letter")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getChristmasLetterRequest()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.code").value("failed.request.body.validation"))
                .andExpect(jsonPath("$.reasons").isArray())
                .andExpect(jsonPath("$.reasons", hasItem("email.must.be.valid")))
                .andExpect(jsonPath("$.reasons", hasItem("name.cannot.be.null")))
                .andExpect(jsonPath("$.reasons", hasItem("name.cannot.be.empty")))
                .andExpect(jsonPath("$.reasons", hasItem("must.add.at.least.one.wish")))
                .andExpect(jsonPath("$.reasons", hasItem("address.cannot.be.null")))
                .andExpect(jsonPath("$.reasons", hasItem("address.cannot.be.empty")));
    }

    public String getChristmasLetterRequest() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        return mapper.writeValueAsString(ChristmasLetterRequest.builder()
                        .email("invalid_email")
                        .name(null)
                        .wishes(new ArrayList<>())
                        .deliveryAddress(null)
                .build());
    }

}
