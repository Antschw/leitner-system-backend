package com.esgi.leitner.application.service;

import com.esgi.leitner.application.port.in.CardService;
import com.esgi.leitner.application.port.out.CardRepository;
import com.esgi.leitner.domain.factory.CardFactory;
import com.esgi.leitner.domain.model.Card;
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
     * Creates a new Card and ensures it has a valid ID before saving.
     * <p>
     * If the provided Card does not have an ID, a new one is generated using {@link CardFactory}.
     *
     * @param card The card to be created and stored.
     * @return The card with a valid ID.
     */
    @Override
    public Card createCard(Card card) {
        if (card.getId() == null || card.getId().isBlank()) {
            card = CardFactory.createCard(card.getQuestion(), card.getAnswer(), card.getTag());
        }
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
    /**
     * Retrieves a card with a given ID
     *
     * @param cardId the ID of the searched card
     * @return List of matching cards.
     */
    @Override
    public Card getCardById(String cardId) {
        return cardRepository.findById(cardId).orElse(null);
    }

}
