package com.esgi.leitner.domain.service;

import com.esgi.leitner.domain.model.Token;
import com.esgi.leitner.domain.model.User;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    public User createUser(User user) {
        return null;
    }

    public Optional<User> getUserById(String id) {
        return null;
    }

    public List<User> getAllUsers() {
        return null;
    }

    public User updateUser(User user) {
        return null;
    }

    public void deleteUser(String id) {
    }

    public Token generateTokenForUser(User user) {
        return null;
    }

    public User cooldownCardUser(User user) {
        return null;
    }

    /**
     * Update the date where the user will receive the notification
     *
     * @param userId the user id
     * @param notificationTime the new reception date of the notification
     * @return the user updated
     * @throws RuntimeException if user not found.
     */
    public User updateNotificationTime(String userId, LocalTime notificationTime) {
        Optional<User> userOpt = getUserById(userId);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.setNotificationTime(notificationTime);
            return updateUser(user);
        }
        throw new RuntimeException("User not found : " + userId);
    }
}
