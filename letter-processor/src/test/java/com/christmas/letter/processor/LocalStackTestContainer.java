package com.christmas.letter.processor;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import org.testcontainers.utility.MountableFile;

@Testcontainers
public class LocalStackTestContainer {

    private static final String LOCALSTACK_IMAGE = "localstack/localstack:3.4";
    private static final String INIT_FILE_PATH = "/etc/localstack/init/ready.d/localstack-test-setup.sh";
    private static final String MAILPIT_IMAGE = "axllent/mailpit:v1.15";
    private static final String REDIS_IMAGE = "redis/redis-stack:latest";


    protected static final LocalStackContainer localStackContainer = new LocalStackContainer(DockerImageName.parse(LOCALSTACK_IMAGE))
            .withCopyToContainer(MountableFile.forClasspathResource("localstack-test-setup.sh", 0744), INIT_FILE_PATH)
            .withServices(LocalStackContainer.Service.SNS, LocalStackContainer.Service.SQS, LocalStackContainer.Service.DYNAMODB)
            .waitingFor(Wait.forLogMessage(".*Initialization completed.*",1));

    @Container
    protected static final GenericContainer<?> mailpitContainer = new GenericContainer<>(DockerImageName.parse(MAILPIT_IMAGE))
            .withExposedPorts(1025, 8025)
            .waitingFor(Wait.forLogMessage(".*accessible via.*", 1));

    @Container
    protected static final GenericContainer<?> redisContainer = new GenericContainer<>(DockerImageName.parse(REDIS_IMAGE))
            .withExposedPorts(6379, 8001);

    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry) {
        registry.add("spring.cloud.aws.credentials.access-key", localStackContainer::getAccessKey);
        registry.add("spring.cloud.aws.credentials.secret-key", localStackContainer::getSecretKey);

        registry.add("spring.cloud.aws.sns.endpoint", localStackContainer::getEndpoint);
        registry.add("spring.cloud.aws.sns.region", localStackContainer::getRegion);

        registry.add("spring.cloud.aws.sqs.region", localStackContainer::getRegion);
        registry.add("spring.cloud.aws.sqs.endpoint", localStackContainer::getEndpoint);

        registry.add("spring.cloud.aws.dynamodb.region", localStackContainer::getRegion);
        registry.add("spring.cloud.aws.dynamodb.url", () -> localStackContainer.getEndpointOverride(LocalStackContainer.Service.DYNAMODB).toString());

        registry.add("spring.mail.host", mailpitContainer::getHost);
        registry.add("spring.mail.port", mailpitContainer::getFirstMappedPort);
        registry.add("mailpit.web.port", () -> mailpitContainer.getMappedPort(8025));

        System.setProperty("spring.data.redis.host", redisContainer.getHost());
        System.setProperty("spring.data.redis.port", redisContainer.getMappedPort(6379).toString());
    }

    @BeforeAll
    static void startLocalStackContainer() {
        if (!localStackContainer.isRunning())
            localStackContainer.start();

        if(!mailpitContainer.isRunning())
            mailpitContainer.start();

        if(!redisContainer.isRunning())
            redisContainer.start();
    }

    @AfterAll
    static void stopLocalStackContainer() {
        if(localStackContainer.isRunning())
            localStackContainer.stop();

        if(mailpitContainer.isRunning())
            mailpitContainer.stop();

        if(redisContainer.isRunning())
            redisContainer.stop();
    }
}
