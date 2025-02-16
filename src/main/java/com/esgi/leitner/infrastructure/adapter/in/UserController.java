package com.esgi.leitner.infrastructure.adapter.in;

import com.esgi.leitner.domain.model.User;
import com.esgi.leitner.domain.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Creates (sign up) a new user.
     * Example: POST /users/signup
     *
     * @param user the user details in the request body
     * @return the created user
     */
    @PostMapping("/signup")
    public User signUp(@RequestBody User user) {
        return userService.createUser(user);
    }

    /**
     * Logs in a user.
     * Example: POST /users/login
     *
     * @param user the user credentials in the request body (email, password, etc.)
     * @return a token or a simple message
     */
    @PostMapping("/login")
    public String login(@RequestBody User user) {
        return "User " + user.getUsername() + " logged in.";
    }

    /**
     * Disconnects a user (logout).
     * Example: POST /users/disconnect?userId=123
     *
     * @param userId the ID of the user to disconnect
     * @return a simple message indicating the user is disconnected
     */
    @PostMapping("/disconnect")
    public String disconnect(@RequestParam("userId") String userId) {
        return "User " + userId + " disconnected";
    }

    /**
     * Retrieves a user by ID.
     * Example: GET /users/{userId}
     *
     * @param userId the ID of the user
     * @return the user found
     * @throws RuntimeException if the user is not found
     */
    @GetMapping("/{userId}")
    public User getUserById(@PathVariable("userId") String userId) {
        return userService.getUserById(userId)
                .orElseThrow(() -> new RuntimeException("User not found: " + userId));
    }

    /**
     * Retrieves all users.
     * Example: GET /users
     *
     * @return a list of all users
     */
    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    /**
     * Updates an existing user.
     * Example: PUT /users/{userId}
     *
     * @param userId the ID of the user to update
     * @param user   the updated user details
     * @return the updated user
     */
    @PutMapping("/{userId}")
    public User updateUser(@PathVariable("userId") String userId, @RequestBody User user) {
        user.setId(userId);
        return userService.updateUser(user);
    }

    /**
     * Deletes a user.
     * Example: DELETE /users/{userId}
     *
     * @param userId the ID of the user to delete
     */
    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable("userId") String userId) {
        userService.deleteUser(userId);
    }


    /**
     * Applies cooldown to the user.
     * Example: PATCH /users/{userId}/cooldown
     *
     * @param userId the ID of the user
     * @param user   the user details (should match the ID in the path)
     * @return the updated user with cooldown applied
     */
    @PatchMapping("/{userId}/cooldown")
    public User cooldownCardUser(@PathVariable("userId") String userId, @RequestBody User user) {
        user.setId(userId);
        return userService.cooldownCardUser(user);
    }

    /**
     * Updates the notification time for the user.
     * Example: PATCH /users/{userId}/notification-time?time=15:30:00
     *
     * @param userId the ID of the user
     * @param time   the new notification time in HH:mm:ss format
     * @return the updated user
     */
    @PatchMapping("/{userId}/notification-time")
    public User updateNotificationTime(@PathVariable("userId") String userId,
                                       @RequestParam("time") String time) {
        LocalTime notificationTime = LocalTime.parse(time);
        return userService.updateNotificationTime(userId, notificationTime);
    }



}
