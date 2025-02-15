package com.esgi.leitner.domain.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class QuizNotification {
    private String id;
    private String content;
    private List<User> recipients;
    private Status status;

}
