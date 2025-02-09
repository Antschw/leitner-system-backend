package com.esgi.leitner.domain.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Card {
    private String id;
    private String question;
    private String answer;
    private String tag;
    private Category category;
}
