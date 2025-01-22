package com.christmas.letter.processor.service;

import com.christmas.letter.config.RedisCacheConfig;
import com.christmas.letter.processor.LocalStackTestContainer;
import com.christmas.letter.processor.helper.LetterTestHelper;
import com.christmas.letter.repository.LetterRepository;
import com.christmas.letter.service.LetterProcessorService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.test.context.TestPropertySource;

import java.util.Objects;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
@TestPropertySource("classpath:application.yml")
public class LetterProcessorServiceIntegrationTest extends LocalStackTestContainer {

    @Autowired
    private LetterRepository letterRepository;

    @Autowired
    private LetterProcessorService letterProcessorService;

    @Autowired
    private CacheManager cacheManager;

    @BeforeEach
    public void init() {
        Cache cache = cacheManager.getCache(RedisCacheConfig.LETTER_CACHE_NAME);
        assert cache != null;
        cache.put(RedisCacheConfig.LETTER_CACHE_KEY, LetterTestHelper.createDefaultLetter());
    }

    @AfterEach
    public void cleanUp() {
        for(String name : cacheManager.getCacheNames()){
            Objects.requireNonNull(cacheManager.getCache(name)).clear();
        }
    }

    @Test
    public void getLetterByEmail_WhenLetterIsCached_ShouldNotCallDB() {
        letterProcessorService.getLetterByEmail("existing@email.com");

        verify(letterRepository.findById("existing@email.com"), times(0));
    }

}
