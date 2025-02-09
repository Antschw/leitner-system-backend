package com.esgi.leitner.domain.model;

import java.util.List;

public class Notification {
    private String id;
    private String content;
    private List<User> recipient; // Destinataire
    private String status;

}
