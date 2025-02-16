package com.esgi.leitner.domain.service;

import com.esgi.leitner.application.port.in.CardService;
import com.esgi.leitner.domain.model.Card;
import com.esgi.leitner.domain.model.User;
import com.esgi.leitner.infrastructure.dto.QuizAnswerRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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
     * Démarre un quiz pour un utilisateur s'il n'en a pas déjà passé un aujourd'hui.
     *
     * @param userId l'identifiant de l'utilisateur
     * @throws RuntimeException si l'utilisateur a déjà passé le quiz aujourd'hui
     */
    public void startQuiz(String userId) {
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
    }
    /**
     * Process the answer provided in a quiz session.
     *
     * @param cardId  the ID of the card being answered.
     * @param request the quiz answer request containing the user's answer and forceValidation flag.
     * @return a message indicating the outcome.
     */
    public String processAnswer(String cardId, QuizAnswerRequest request) {
        Card card = cardService.getCardById(cardId);
        if (card == null) {
            return "Card not found.";
        }
        if (card.getAnswer() == null) {
            return "Card answer not defined.";
        }
        boolean isCorrect = card.getAnswer().trim().equalsIgnoreCase(request.getUserAnswer().trim());
        if (!isCorrect && !request.isForceValidation()) {
            return "Incorrect answer. The correct answer is: " + card.getAnswer();
        }
        cardService.answerCard(cardId, true);
        return "Answer accepted.";
    }
}
