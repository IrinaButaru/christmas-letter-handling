package com.christmas.letter.config.dynamodb;

import com.christmas.letter.model.ChristmasLetterEntity;
import io.awspring.cloud.dynamodb.DynamoDbTableNameResolver;
import jakarta.validation.constraints.NotNull;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class ChristmasLetterTableNameResolver implements DynamoDbTableNameResolver {

    @Override
    public <T> String resolve(@NotNull Class<T> clazz) {
        return classTableNameMap().get(clazz);
    }

    private Map<Class<?>, String> classTableNameMap() {
        return Map.of(ChristmasLetterEntity.class, ChristmasLetterEntity.TABLE_NAME);
    }
}
