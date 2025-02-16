package com.esgi.leitner.infrastructure.adapter.in;

import com.esgi.leitner.domain.model.Card;
import com.esgi.leitner.domain.service.QuizManager;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/quiz")
public class QuizController {

    private final QuizManager quizManager;

    public QuizController(QuizManager quizManager) {
        this.quizManager = quizManager;
    }

    /**
     * Creates a quiz for a user and returns the cards to review (without their answers).
     * Example: POST /quiz/create?userId=123
     *
     * @param userId the user's identifier
     * @return the list of cards without the answer
     */
    @PostMapping("/create")
    public ResponseEntity<List<Card>> createQuiz(@RequestParam("userId") String userId) {
        try {
            List<Card> quizCards = quizManager.createQuiz(userId);
            return ResponseEntity.ok(quizCards);
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().build();
        }
    }
}