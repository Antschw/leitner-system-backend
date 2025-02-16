package com.esgi.leitner.infrastructure.adapter.in;

import com.esgi.leitner.application.port.in.CardService;
import com.esgi.leitner.domain.service.QuizManager;
import com.esgi.leitner.infrastructure.dto.QuizAnswerRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/quiz")
public class QuizController {

    private final QuizManager quizManager;


    public QuizController(QuizManager quizManager) {
        this.quizManager = quizManager;
    }

    /**
     * Starts a quiz session for a given user if they haven't taken one today.
     * Example: POST /quiz/start?userId=123
     *
     * @param userId the user's id
     * @return a response indicating whether the quiz has been started successfully or an error message.
     */
    @PostMapping("/start")
    public ResponseEntity<String> startQuiz(@RequestParam("userId") String userId) {
        try {
            quizManager.startQuiz(userId);
            return ResponseEntity.ok("Quiz started successfully.");
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

    /**
     * Answers a quiz question.
     * Compares the user's answer with the correct answer and allows forced validation.
     * Example: PATCH /quiz/{cardId}/answer
     *
     * @param cardId  the ID of the card being answered
     * @param request contains the user's answer and the forceValidation flag
     * @return ResponseEntity with a message indicating the result.
     */
    @PatchMapping("/{cardId}/answer")
    public ResponseEntity<String> answerQuestion(@PathVariable("cardId") String cardId,
                                                 @Valid @RequestBody QuizAnswerRequest request) {
        String result = quizManager.processAnswer(cardId, request);
        return ResponseEntity.ok(result);
    }
}
