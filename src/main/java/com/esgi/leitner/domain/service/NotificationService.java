package com.esgi.leitner.domain.service;

import com.esgi.leitner.domain.model.QuizNotification;
import com.esgi.leitner.domain.model.Status;
import com.esgi.leitner.domain.model.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
@Service
public class NotificationService {

    /**
     * Create a notification in order to remind to play at the quiz.
     *
     * @param content Content of the notification.
     * @param recipients List of users who will receive the notifications.
     * @return Notification created.
     */
    public QuizNotification createNotification(String content, List<User> recipients) {
        QuizNotification notification = new QuizNotification();
        notification.setId(UUID.randomUUID().toString());
        notification.setContent(content);
        notification.setRecipients(recipients);
        notification.setStatus(Status.CREATED);
        return notification;
    }

    /**
     *  Send the notification.
     *
     * @param notification The notification which need to be sent.
     */
    public void sendNotification(QuizNotification notification) {
        notification.setStatus(Status.SENT);
    }
}
