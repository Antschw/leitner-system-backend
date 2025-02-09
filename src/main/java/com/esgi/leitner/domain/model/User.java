package com.esgi.leitner.domain.model;

import java.util.List;

public class User {
    private String id;
    private String username;
    private String email;
    private String password;
    private Token token;
    private List<Card> cardList;
    private boolean cooldown;

}
