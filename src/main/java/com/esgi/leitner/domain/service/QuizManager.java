package com.esgi.leitner.domain.service;

import com.esgi.leitner.application.port.in.CardService;
import com.esgi.leitner.domain.model.Card;
import com.esgi.leitner.domain.model.Category;
import com.esgi.leitner.domain.model.User;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
@Service
public class QuizManager {

    private final UserService userService;
    private final CardService cardService;

    public QuizManager(UserService userService, CardService cardService) {
        this.userService = userService;
        this.cardService = cardService;
    }

    /**
     * Checks whether the user has already taken a quiz today, updates the lastQuizDate if necessary,
     * and returns a list of cards to review (categories != DONE).
     *
     * @param userId The user's identifier
     * @return A list of filtered cards (not in DONE) with their answers removed
     */
    public List<Card> createQuiz(String userId) {
        Optional<User> userOpt = userService.getUserById(userId);
        if (userOpt.isEmpty()) {
            throw new RuntimeException("User not found: " + userId);
        }
        User user = userOpt.get();
        LocalDate today = LocalDate.now();
        if (today.equals(user.getLastQuizDate())) {
            throw new RuntimeException("You have already taken a quiz today.");
        }
        user.setLastQuizDate(today);
        userService.updateUser(user);
        List<Card> allQuizCards = cardService.getQuizzCards(null);
        List<Card> filtered = allQuizCards.stream()
                .filter(card -> card.getCategory() != Category.DONE)
                .toList();
        filtered.forEach(card -> card.setAnswer(null));

        return filtered;
    }
}
