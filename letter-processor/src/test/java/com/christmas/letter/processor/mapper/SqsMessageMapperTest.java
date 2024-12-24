package com.christmas.letter.processor.mapper;

import com.christmas.letter.model.mapper.SqsMessageMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class SqsMessageMapperTest {

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private SqsMessageMapper sqsMessageMapper;

//    @Test
//    public void getMessageFromBody_WhenInvalidJson_ShouldThrowException() throws JsonProcessingException {
//        ObjectMapper testObjectMapper = new ObjectMapper();
//        String message = "invalid message";
//        JsonNode node = testObjectMapper.readTree("{ \"key\" : \"jsonString\"}");

//        when(objectMapper.readTree(any())).thenThrow(() -> new JsonProcessingException("msj"));
//
//        assertThatThrownBy(() -> sqsMessageMapper.getMessageFromBody(message))
//                .isInstanceOf(SqsMessageDeserializationException.class)
//                .hasMessage("Could not process letter from SQS message");
//    }
}
