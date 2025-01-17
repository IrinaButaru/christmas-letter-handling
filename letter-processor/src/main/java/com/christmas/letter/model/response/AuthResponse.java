package com.christmas.letter.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class AuthResponse {

    private String token;

    private List<String> roles;
}
