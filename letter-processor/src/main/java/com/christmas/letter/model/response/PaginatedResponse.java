package com.christmas.letter.model.response;

import com.christmas.letter.model.Letter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaginatedResponse {

    private List<Letter> letters;

    private Integer totalPages;

    private Boolean last;
}
