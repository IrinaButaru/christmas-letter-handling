package com.christmas.letter.processor.service;

import com.christmas.letter.config.RedisCacheConfig;
import com.christmas.letter.model.dto.Letter;
import com.christmas.letter.processor.LocalStackTestContainer;
import com.christmas.letter.processor.helper.UserTestHelper;
import com.christmas.letter.repository.LetterRepository;
import com.christmas.letter.service.LetterProcessorService;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.cache.CacheManager;
import org.springframework.test.context.TestPropertySource;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

@SpringBootTest
@TestPropertySource("classpath:application.yml")
public class LetterProcessorServiceIntegrationTest extends LocalStackTestContainer {

    @SpyBean
    private LetterRepository letterRepository;

    @Autowired
    private LetterProcessorService letterProcessorService;

    @Autowired
    private CacheManager cacheManager;

    @BeforeAll
    public static void initData(@Autowired LetterRepository letterRepository) {
        letterRepository.saveAll(UserTestHelper.getLetterEntities());
    }

    @BeforeEach
    public void cleanCache() {
        cacheManager.getCache(RedisCacheConfig.LETTER_CACHE_NAME).invalidate();
    }

    @AfterAll
    public static void cleanData(@Autowired LetterRepository letterRepository) {
        letterRepository.deleteAll();
    }


    @Test
    public void getLetterByEmail_WhenLetterIsCached_ShouldNotCallDB() throws InterruptedException {
        String email = "ok@email.com";

        Letter letter = letterProcessorService.getLetterByEmail(email);
        assertNotNull(letter);
        verify(letterRepository).findById(email);

        clearInvocations(letterRepository);
        letter = letterProcessorService.getLetterByEmail(email);
        assertNotNull(letter);
        verifyNoInteractions(letterRepository);
    }

}
