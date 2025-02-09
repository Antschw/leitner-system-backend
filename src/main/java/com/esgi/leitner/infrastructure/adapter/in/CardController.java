package com.esgi.leitner.infrastructure.adapter.in;

import com.esgi.leitner.application.port.in.CardService;
import com.esgi.leitner.domain.model.Card;
import com.esgi.leitner.infrastructure.dto.AnswerRequest;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller to manage cards in the Leitner System.
 */
@RestController
@RequestMapping("/cards")
@Validated
public class CardController {

    private final CardService cardService;

    public CardController(CardService cardService) {
        this.cardService = cardService;
    }

    /**
     * Retrieves all cards or filters them by tags if provided.
     *
     * @param tags Optional list of tags to filter cards.
     * @return List of cards.
     */
    @GetMapping
    public List<Card> getAllCards(@RequestParam(name = "tags", required = false) List<String> tags) {
        return cardService.getAllCards(tags);
    }

    /**
     * Creates a new card.
     *
     * @param card The card details.
     * @return The created card.
     */
    @PostMapping
    public Card createCard(@Valid @RequestBody Card card) {
        return cardService.createCard(card);
    }

    /**
     * Retrieves cards scheduled for a quiz session.
     *
     * @param date Optional date for the quiz (defaults to today).
     * @return List of quiz cards.
     */
    @GetMapping("/quizz")
    public List<Card> getQuizzCards(@RequestParam(name = "date", required = false) String date) {
        return cardService.getQuizzCards(date);
    }

    /**
     * Submits an answer for a specific card.
     *
     * @param cardId  The ID of the card being answered.
     * @param request The answer validation request.
     */
    @PatchMapping("/{cardId}/answer")
    public void answerCard(@PathVariable("cardId") String cardId, @Valid @RequestBody AnswerRequest request) {
        cardService.answerCard(cardId, request.isValid());
    }
}
