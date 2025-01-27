package com.christmas.letter.service;

import com.christmas.letter.config.RedisCacheConfig;
import com.christmas.letter.exception.GlobalExceptionHandler;
import com.christmas.letter.exception.NotFoundException;
import com.christmas.letter.model.dto.Letter;
import com.christmas.letter.model.entity.LetterEntity;
import com.christmas.letter.model.mapper.DynamoDbLetterMapper;
import com.christmas.letter.model.response.PaginatedResponse;
import com.christmas.letter.repository.LetterRepository;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class LetterProcessorService {

    private final LetterRepository letterRepository;

//    @CachePut(value = RedisCacheConfig.LETTER_CACHE_NAME, key = RedisCacheConfig.NEW_LETTER_CACHE_KEY)
    public Letter saveLetter(LetterEntity letterEntity) {
        return DynamoDbLetterMapper.INSTANCE.entitytoObject(letterRepository.save(letterEntity));
    }

    @Cacheable(value = RedisCacheConfig.LETTER_CACHE_NAME, key = RedisCacheConfig.LETTER_CACHE_KEY)
    public Letter getLetterByEmail(String email) {
        LetterEntity letterEntity = letterRepository.findById(email)
                .orElseThrow(() -> new NotFoundException(String.format(GlobalExceptionHandler.LETTER_NOT_FOUND_MESSAGE, email)));

        return DynamoDbLetterMapper.INSTANCE.entitytoObject(letterEntity);
    }

    public PaginatedResponse getAllLetters(Pageable pageable) {
        Page<LetterEntity> page = letterRepository.findAll(pageable);

        List<Letter> letters = page
                .getContent()
                .stream()
                .map(DynamoDbLetterMapper.INSTANCE::entitytoObject)
                .toList();

        return PaginatedResponse.builder()
                .letters(letters)
                .totalPages(page.getTotalPages())
                .last(page.isLast())
                .build();
    }
}
