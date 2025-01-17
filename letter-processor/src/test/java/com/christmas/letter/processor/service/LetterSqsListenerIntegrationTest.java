package com.christmas.letter.processor.service;

import com.christmas.letter.model.dto.Letter;
import com.christmas.letter.model.entity.LetterEntity;
import com.christmas.letter.processor.LocalStackTestContainer;
import com.christmas.letter.processor.helper.LetterTestHelper;
import com.christmas.letter.processor.helper.MailpitClient;
import com.christmas.letter.processor.helper.MailpitConfig;
import com.christmas.letter.repository.LetterRepository;
import com.christmas.letter.service.LetterEmailService;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.awspring.cloud.sns.core.SnsTemplate;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.test.context.TestPropertySource;

import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.awaitility.Awaitility.await;
import static org.testcontainers.shaded.org.apache.commons.io.ThreadUtils.sleep;

@SpringBootTest(classes = {MailpitConfig.class})
@TestPropertySource("classpath:application.yml")
@ExtendWith(OutputCaptureExtension.class)
public class LetterSqsListenerIntegrationTest extends LocalStackTestContainer {
    private static final String TOPIC_ARN = "arn:aws:sns:us-east-1:000000000000:test-topic";

    @Value("${com.christmas.letter.aws.sqs.dlq-url}")
    private String dlqUrl;

    @Value("${spring.mail.username}")
    private String sender;

    @Value("${com.christmas.letter.email.recipients}")
    private List<String> recipients;

    @Autowired
    private SnsTemplate snsTemplate;

    @Autowired
    private LetterRepository letterRepository;

    @Autowired
    private MailpitClient mailpitClient;

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
    public void processLetter_WhenMessageNotOk_ShouldNotStoreLetterAndSendEmail(CapturedOutput output) throws InterruptedException {
        Letter letter = LetterTestHelper.createLetter("wrong", "Wrong letter");
        snsTemplate.convertAndSend(TOPIC_ARN, letter);

        sleep(Duration.ofSeconds(1));

        Optional<LetterEntity> letterEntity = letterRepository.findById(letter.getEmail());
        assert letterEntity.isEmpty();

        await().atMost(40, TimeUnit.SECONDS)
                .until(() -> output.getAll().contains(LetterEmailService.SENT_EMAIL_LOG));

        ObjectNode result = mailpitClient.findFirstMessage();
        assertSoftly(softly -> {
            softly.assertThat(result.get("From").get("Address").asText()).isEqualTo(sender);
            softly.assertThat(result.get("Subject").asText()).isEqualTo(LetterEmailService.EMAIL_SUBJECT);
//            softly.assertThat(result.get("Text").asText()).contains(LetterEmailService.EMAIL_BODY_TEMPLATE);
            //TODO: verify recipients and email text are as expected
        });
    }
}
