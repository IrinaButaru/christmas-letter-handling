package com.christmas.letter.sender.controller;

import com.christmas.letter.sender.dto.ChristmasLetter;
import com.christmas.letter.sender.service.LetterSenderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.shaded.com.fasterxml.jackson.core.JsonProcessingException;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import software.amazon.awssdk.services.sns.model.NotFoundException;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
public class LetterSenderControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LetterSenderService letterSenderService;

    @Test
    public void publishLetter_WhenBadRequest_ShouldReturnErrorResponse() throws Exception {
        mockMvc.perform(post("/letter")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getBadChristmasLetterRequest()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.code").value("failed.request.body.validation"))
                .andExpect(jsonPath("$.reasons").isArray())
                .andExpect(jsonPath("$.reasons", hasItem("email.must.be.valid")))
                .andExpect(jsonPath("$.reasons", hasItem("name.cannot.be.empty")))
                .andExpect(jsonPath("$.reasons", hasItem("must.add.at.least.one.wish")))
                .andExpect(jsonPath("$.reasons", hasItem("address.cannot.be.empty")));
    }

    @Test
    public void publishLetter_WhenAWSSNSTopicNotFound_ShouldReturnErrorResponse() throws Exception {

        ChristmasLetter requestBody = getChristmasLetterRequestJSON();
        String requestBodyString = getChristmasLetterRequest();
        doThrow(NotFoundException.class).when(letterSenderService).publishLetter(requestBody);

        mockMvc.perform(post("/letter")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBodyString))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.code").value("aws.sns.topic.does.not.exist"));
    }

    @Test
    public void publishLetter_WhenOK_ShouldReturnSuccessResponse() throws Exception {
        mockMvc.perform(post("/letter")
                .contentType(MediaType.APPLICATION_JSON)
                .content(getChristmasLetterRequest()))
                .andExpect(status().isOk());

    }

    public String getBadChristmasLetterRequest() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        return mapper.writeValueAsString(ChristmasLetter.builder()
                        .email("invalid_email")
                        .name(null)
                        .wishes(new ArrayList<>())
                        .deliveryAddress(null)
                .build());
    }

    public String getChristmasLetterRequest() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        return mapper.writeValueAsString(getChristmasLetterRequestJSON());
    }

    public ChristmasLetter getChristmasLetterRequestJSON()  {

        return ChristmasLetter.builder()
                .email("test@email.com")
                .name("Test")
                .wishes(List.of("Item1", "Item2"))
                .deliveryAddress("Test Street no. 7")
                .build();
    }

}
