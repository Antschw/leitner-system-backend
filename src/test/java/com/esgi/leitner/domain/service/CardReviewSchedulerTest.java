package com.esgi.leitner.domain.service;

import com.esgi.leitner.domain.model.Card;
import com.esgi.leitner.domain.model.Category;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class CardReviewSchedulerTest {

    private CardReviewScheduler cardReviewScheduler;

    @BeforeEach
    void setUp() {
        cardReviewScheduler = new CardReviewScheduler();
    }

    @Test
    void shouldReturnEmptyListWhenNoCardsExist() {
        List<Card> reviewCards = cardReviewScheduler.selectCardsForReview(List.of(), LocalDate.now());
        assertThat(reviewCards).isEmpty();
    }

    @Test
    void shouldSelectCardsScheduledForReviewToday() {
        LocalDate today = LocalDate.ofEpochDay(16);
        Card dueCard = createCard(Category.FIFTH);

        List<Card> reviewCards = cardReviewScheduler.selectCardsForReview(List.of(dueCard), today);

        assertThat(reviewCards).containsExactly(dueCard);
    }

    @Test
    void shouldNotSelectCardsThatAreNotDueForReview() {
        LocalDate today = LocalDate.ofEpochDay(3);
        Card notDueCard = createCard(Category.THIRD);

        List<Card> reviewCards = cardReviewScheduler.selectCardsForReview(List.of(notDueCard), today);

        assertThat(reviewCards).isEmpty();
    }

    @Test
    void shouldSelectMultipleCardsThatAreDueForReview() {
        LocalDate today = LocalDate.ofEpochDay(8);
        Card card1 = createCard(Category.SECOND);
        Card card2 = createCard(Category.FOURTH);
        Card card3 = createCard(Category.FIFTH);

        List<Card> reviewCards = cardReviewScheduler.selectCardsForReview(List.of(card1, card2, card3), today);

        assertThat(reviewCards).containsExactlyInAnyOrder(card1, card2);
    }

    @Test
    void shouldNotSelectAnyCardIfNoneAreDueForReview() {
        LocalDate today = LocalDate.ofEpochDay(7);
        Card card1 = createCard(Category.FOURTH);
        Card card2 = createCard(Category.FIFTH);

        List<Card> reviewCards = cardReviewScheduler.selectCardsForReview(List.of(card1, card2), today);

        assertThat(reviewCards).isEmpty();
    }

    private Card createCard(Category category) {
        Card card = new Card();
        card.setCategory(category);
        return card;
    }
}
