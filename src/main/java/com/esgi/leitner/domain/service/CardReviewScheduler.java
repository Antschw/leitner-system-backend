package com.esgi.leitner.domain.service;

import com.esgi.leitner.domain.model.Card;
import com.esgi.leitner.domain.model.Category;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Determines which cards should be reviewed on a specific date.
 * Implements the Leitner system to schedule card reviews based on their category.
 */
@Component
public class CardReviewScheduler {

    private static final Map<Category, Integer> CATEGORY_INTERVALS = Map.of(
            Category.FIRST, 1,
            Category.SECOND, 2,
            Category.THIRD, 4,
            Category.FOURTH, 8,
            Category.FIFTH, 16,
            Category.SIXTH, 32,
            Category.SEVENTH, 64
    );

    /**
     * Filters the given list of cards to select those that should be reviewed on the specified date.
     *
     * @param allCards The list of all stored cards.
     * @param quizDate The date for which the review session is scheduled.
     * @return A list of cards that should be reviewed on the given date.
     */
    public List<Card> selectCardsForReview(List<Card> allCards, LocalDate quizDate) {
        return allCards.stream()
                .filter(card -> shouldAppearToday(card, quizDate))
                .collect(Collectors.toList());
    }

    /**
     * Determines if a given card should be reviewed on the specified date based on its category.
     *
     * <p>In the Leitner system, each category has a predefined review interval:
     * Category 1 → 1 day, Category 2 → 2 days, Category 3 → 4 days, etc.
     *
     * @param card  The card to evaluate for review.
     * @param today The date to check.
     * @return {@code true} if the card should be reviewed on the given date, {@code false} otherwise.
     *
     * @implNote A card is due for review if {@code today.toEpochDay()} is a multiple of its category's interval,
     *           ensuring that review dates align correctly. Day 0 is excluded to avoid unintended matches.
     */
    private boolean shouldAppearToday(Card card, LocalDate today) {
        int interval = CATEGORY_INTERVALS.getOrDefault(card.getCategory(), Integer.MAX_VALUE);

        return (today.toEpochDay() % interval) == 0 && today.toEpochDay() != 0;
    }




}
