package com.esgi.leitner.domain.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
public class User {
    // Getters et setters
    private String id;
    private String username;
    private String email;
    private String password;
    private Token token;
    private List<Card> cardList;
    private boolean cooldown;
    private LocalTime notificationTime;
    private LocalDate lastQuizDate;
}
