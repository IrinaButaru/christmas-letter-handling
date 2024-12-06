package com.christmas.letter.sender.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorDto {

    private Integer status;

    private String code;

    private List<String> reasons;
}
