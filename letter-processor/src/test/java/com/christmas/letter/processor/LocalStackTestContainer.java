package com.christmas.letter.processor;

import org.junit.jupiter.api.BeforeAll;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import org.testcontainers.utility.MountableFile;

@Testcontainers
public class LocalStackTestContainer {

    private static final String LOCALSTACK_IMAGE = "localstack/localstack:3.4";
    private static final String INIT_FILE_PATH = "/etc/localstack/init/ready.d/localstack-test-setup.sh";

    protected static final LocalStackContainer localStackContainer = new LocalStackContainer(DockerImageName.parse(LOCALSTACK_IMAGE))
            .withCopyToContainer(MountableFile.forClasspathResource("localstack-test-setup.sh", 0744), INIT_FILE_PATH)
            .withServices(LocalStackContainer.Service.SNS, LocalStackContainer.Service.SQS, LocalStackContainer.Service.DYNAMODB)
            .waitingFor(Wait.forLogMessage(".*Initialization completed.*",1));

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
    }

    @BeforeAll
    static void startLocalStackContainer() {
        if (!localStackContainer.isRunning())
            localStackContainer.start();
    }

//    @AfterAll
//    static void stopLocalStackContainer() {
//        if(localStackContainer.isRunning())
//            localStackContainer.stop();
//    }
}
