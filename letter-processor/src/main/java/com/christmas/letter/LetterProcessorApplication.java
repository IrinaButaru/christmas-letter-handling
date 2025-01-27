package com.christmas.letter;

import org.socialsignin.spring.data.dynamodb.repository.config.EnableDynamoDBRepositories;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.stereotype.Repository;

@SpringBootApplication
@EnableDynamoDBRepositories(includeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = Repository.class))
@EnableCaching
public class LetterProcessorApplication {
    public static void main(String[] args) {
        SpringApplication.run(LetterProcessorApplication.class, args);
    }
}
