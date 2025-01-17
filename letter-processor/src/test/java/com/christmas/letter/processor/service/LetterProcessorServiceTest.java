package com.christmas.letter.processor.service;

import com.christmas.letter.exception.GlobalExceptionHandler;
import com.christmas.letter.exception.NotFoundException;
import com.christmas.letter.model.dto.Letter;
import com.christmas.letter.model.entity.LetterEntity;
import com.christmas.letter.model.mapper.DynamoDbLetterMapper;
import com.christmas.letter.model.response.PaginatedResponse;
import com.christmas.letter.processor.helper.LetterTestHelper;
import com.christmas.letter.repository.LetterRepository;
import com.christmas.letter.service.LetterProcessorService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class LetterProcessorServiceTest {

    @Mock
    private LetterRepository letterRepository;

    @InjectMocks
    private LetterProcessorService letterProcessorService;

    @Test
    void getLetterByEmail_WhenCorrectEmail_ShouldReturnLetter() {
        Letter letter = LetterTestHelper.createDefaultLetter();
        LetterEntity letterEntity = DynamoDbLetterMapper.INSTANCE.objectToEntity(letter);

        when(letterRepository.findById(letter.getEmail())).thenReturn(Optional.of(letterEntity));

        Letter returnedLetter = letterProcessorService.getLetterByEmail(letter.getEmail());

        verify(letterRepository).findById(letter.getEmail());
        assertThat(returnedLetter).isEqualTo(letter);
    }

    @Test
    void getLetterByEmail_WhenNotFoundEmail_ShouldThrowException(){
        String email = "inexisting@email.com";
        when(letterRepository.findById(email)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> letterProcessorService.getLetterByEmail(email))
                .isInstanceOf(NotFoundException.class)
                .hasMessage(String.format(GlobalExceptionHandler.LETTER_NOT_FOUND_MESSAGE, email));
    }

    @Test
    void getAllLetters_WhenPageRequested_ShouldReturnPaginatedResponse() {
        Pageable pageable = Pageable.ofSize(10).withPage(0);
        List<Letter> letters = LetterTestHelper.createLetters(10);
        List<LetterEntity> entities = letters.stream().map(DynamoDbLetterMapper.INSTANCE::objectToEntity).toList();
        Page<LetterEntity> page = new PageImpl<>(entities);
        when(letterRepository.findAll(pageable)).thenReturn(page);

        PaginatedResponse response = letterProcessorService.getAllLetters(pageable);

        assertThat(response.getLast()).isTrue();
        assertThat(response.getTotalPages()).isEqualTo(1);
        assertThat(response.getLetters()).isEqualTo(letters);
    }

    @Test
    public void saveLetter_WhenValidLetter_ShouldSaveToDynamoDB(){
        Letter letter = LetterTestHelper.createDefaultLetter();
        LetterEntity entity = DynamoDbLetterMapper.INSTANCE.objectToEntity(letter);
        when(letterRepository.save(entity)).thenReturn(entity);

        Letter savedLetter = letterProcessorService.saveLetter(entity);

        assertThat(savedLetter).isEqualTo(letter);
    }
}
