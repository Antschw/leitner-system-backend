package com.esgi.leitner.application.service;

import com.esgi.leitner.application.port.in.CardService;
import com.esgi.leitner.application.port.out.CardRepository;
import com.esgi.leitner.domain.model.Card;
import com.esgi.leitner.domain.model.Category;
import com.esgi.leitner.domain.service.CardReviewScheduler;
import com.esgi.leitner.domain.service.CategoryProgression;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

/**
 * Orchestrates card management in the Leitner system.
 * Delegates business logic to domain services for category progression and review scheduling.
 */
@Service
public class CardServiceImpl implements CardService {

    private final CardRepository cardRepository;
    private final CategoryProgression categoryProgression;
    private final CardReviewScheduler cardReviewScheduler;

    public CardServiceImpl(CardRepository cardRepository, CategoryProgression categoryProgression, CardReviewScheduler cardReviewScheduler) {
        this.cardRepository = cardRepository;
        this.categoryProgression = categoryProgression;
        this.cardReviewScheduler = cardReviewScheduler;
    }

    /**
     * Retrieves all cards, optionally filtered by tags.
     *
     * @param tags List of tags to filter the cards. If null or empty, all cards are returned.
     * @return List of matching cards.
     */
    @Override
    public List<Card> getAllCards(List<String> tags) {
        return (tags == null || tags.isEmpty()) ? cardRepository.findAll() : cardRepository.findByTags(tags);
    }

    /**
     * Creates a new card and assigns it to the first category.
     *
     * @param card The card to be created.
     * @return The created card.
     */
    @Override
    public Card createCard(Card card) {
        card.setCategory(Category.FIRST);
        return cardRepository.save(card);
    }

    /**
     * Retrieves cards scheduled for review based on the Leitner system.
     *
     * @param date The date for which to fetch the cards. If null, uses today's date.
     * @return List of cards scheduled for review.
     */
    @Override
    public List<Card> getQuizzCards(String date) {
        LocalDate quizDate = (date != null) ? LocalDate.parse(date) : LocalDate.now();
        return cardReviewScheduler.selectCardsForReview(cardRepository.findAll(), quizDate);
    }

    /**
     * Updates a card's category based on the user's answer.
     *
     * @param cardId  The ID of the answered card.
     * @param isValid Whether the user's answer was correct.
     */
    @Override
    public void answerCard(String cardId, boolean isValid) {
        cardRepository.findById(cardId)
                .ifPresent(card -> {
                    card.setCategory(categoryProgression.getNextCategory(card.getCategory(), isValid));
                    cardRepository.save(card);
                });
    }
}
