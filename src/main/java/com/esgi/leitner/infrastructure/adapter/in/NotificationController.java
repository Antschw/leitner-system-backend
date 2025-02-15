package com.esgi.leitner.infrastructure.adapter.in;

import com.esgi.leitner.domain.model.QuizNotification;
import com.esgi.leitner.domain.service.NotificationService;
import com.esgi.leitner.infrastructure.dto.NotificationRequest;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    /**
     * Creates a new quiz notification.
     * Example: POST /notifications
     *
     * @param request the notification request containing content and recipient IDs
     * @return the created quiz notification
     */
    @PostMapping
    public QuizNotification createNotification(@RequestBody NotificationRequest request) {
        return notificationService.createNotification(request.getContent(), null);
    }

    /**
     * Sends a notification.
     * Example: PATCH /notifications/send
     *
     * @param notification the notification to send (passed in the request body)
     */
    @PatchMapping("/send")
    public void sendNotification(@RequestBody QuizNotification notification) {
        notificationService.sendNotification(notification);
    }
}
