package com.esgi.leitner.infrastructure.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AnswerRequest {
    @JsonProperty("isValid")
    private boolean isValid;
    private boolean forceValidation;

    @JsonCreator
    public AnswerRequest(boolean isValid) {
        this.isValid = isValid;
    }
}
