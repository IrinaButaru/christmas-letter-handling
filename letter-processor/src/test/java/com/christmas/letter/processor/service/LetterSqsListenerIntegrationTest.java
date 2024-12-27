package com.christmas.letter.processor.service;

import com.christmas.letter.model.Letter;
import com.christmas.letter.model.LetterEntity;
import com.christmas.letter.processor.LocalStackTestContainer;
import com.christmas.letter.processor.helper.LetterTestHelper;
import com.christmas.letter.repository.LetterRepository;
import io.awspring.cloud.sns.core.SnsTemplate;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.time.Duration;
import java.util.Optional;

import static org.testcontainers.shaded.org.apache.commons.io.ThreadUtils.sleep;

@SpringBootTest
@TestPropertySource("classpath:application.yml")
public class LetterSqsListenerIntegrationTest extends LocalStackTestContainer {
    private static final String TOPIC_ARN = "arn:aws:sns:us-east-1:000000000000:test-topic";

    @Autowired
    SnsTemplate snsTemplate;

    @Autowired
    LetterRepository letterRepository;

    @AfterAll
    public static void cleanData(@Autowired LetterRepository letterRepository) {
        letterRepository.deleteAll();
    }

    @Test
    public void processLetter_WhenMessageOK_ShouldStoreLetter() throws InterruptedException {
        Letter letter = LetterTestHelper.createDefaultLetter();
        snsTemplate.convertAndSend(TOPIC_ARN, letter);

        sleep(Duration.ofSeconds(1));

       Optional<LetterEntity> letterEntity = letterRepository.findById(letter.getEmail());

        assert letterEntity.isPresent();
    }

    @Test
    public void processLetter_WhenMessageNotOk_ShouldNotStoreLetter() throws InterruptedException {
        Letter letter = LetterTestHelper.createLetter("wrong", "Wrong letter");
        snsTemplate.convertAndSend(TOPIC_ARN, letter);

        sleep(Duration.ofSeconds(1));

        Optional<LetterEntity> letterEntity = letterRepository.findById(letter.getEmail());
        assert letterEntity.isEmpty();
    }
}
