package com.esgi.leitner.infrastructure.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class NotificationRequest {
    private String content;
    private List<String> recipientIds;
}
