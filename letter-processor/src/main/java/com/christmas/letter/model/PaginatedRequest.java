package com.christmas.letter.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.util.Map;

@Data
@AllArgsConstructor
@Builder
public class PaginatedRequest {

    @Builder.Default
    private Integer pageLimit = 10;

    @Builder.Default
    private Map<String, AttributeValue> lastEvaluatedEmail = Map.of(ChristmasLetterEntity.EMAIL_KEY,
                                                                    AttributeValue.fromS(""));

    @Builder.Default
    private boolean previousPage = false;

    public PaginatedRequest(Integer pageLimit, String email, boolean previousPage) {
        this.pageLimit = pageLimit == null || pageLimit == 0 ? 10 : pageLimit;
        this.lastEvaluatedEmail = email != null ?
                Map.of(ChristmasLetterEntity.EMAIL_KEY, AttributeValue.fromS(email)) :
                null;
        this.previousPage = previousPage;
    }
}
