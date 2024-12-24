package com.christmas.letter.processor.service;

import com.christmas.letter.model.Letter;
import com.christmas.letter.model.mapper.DynamoDbLetterMapper;
import com.christmas.letter.processor.helper.LetterTestHelper;
import com.christmas.letter.service.LetterProcessorService;
import com.christmas.letter.service.LetterSqsListener;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class LetterSqsListenerTest {

    @Mock
    private LetterProcessorService letterProcessorService;

    @InjectMocks
    private LetterSqsListener letterSqsListener;

    @Test
    public void processLetter_WhenValidLetter_ShouldSaveToDynamoDB(){
        Letter letter = LetterTestHelper.createDefaultLetter();
        when(letterProcessorService.saveLetter(DynamoDbLetterMapper.INSTANCE.objectToEntity(letter))).thenReturn(letter);

        Letter savedLetter = letterSqsListener.processLetter(letter);

        assertThat(savedLetter).isEqualTo(letter);

    }
}
