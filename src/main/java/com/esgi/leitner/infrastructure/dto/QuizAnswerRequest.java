package com.esgi.leitner.infrastructure.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class QuizAnswerRequest {
    @JsonProperty("userAnswer")
    private String userAnswer;

    @JsonProperty("forceValidation")
    private boolean forceValidation;

    @JsonCreator
    public QuizAnswerRequest(String userAnswer, boolean forceValidation) {
        this.userAnswer = userAnswer;
        this.forceValidation = forceValidation;
    }
}
