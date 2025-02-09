package com.esgi.leitner.infrastructure.adapter.out.storage;

import com.esgi.leitner.domain.model.Card;
import com.esgi.leitner.domain.model.Category;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class InMemoryCardStoreTest {

    private InMemoryCardStore store;

    @BeforeEach
    void setUp() {
        store = new InMemoryCardStore();
    }

    @Test
    void shouldReturnEmptyListWhenNoCardsAreStored() {
        List<Card> result = store.findAll();
        assertThat(result).isEmpty();
    }

    @Test
    void shouldSaveAndRetrieveCard() {
        Card card = new Card();
        card.setId("123");
        card.setQuestion("What is TDD?");
        card.setAnswer("Test-Driven Development");
        card.setTag("Software Engineering");
        card.setCategory(Category.FIRST);

        store.save(card);
        Optional<Card> retrieved = store.findById("123");

        assertThat(retrieved).isPresent();
        assertThat(retrieved.get()).isEqualTo(card);
    }

    @Test
    void shouldReturnAllStoredCards() {
        Card card1 = new Card();
        card1.setId("1");
        store.save(card1);

        Card card2 = new Card();
        card2.setId("2");
        store.save(card2);

        List<Card> allCards = store.findAll();
        assertThat(allCards).hasSize(2).containsExactlyInAnyOrder(card1, card2);
    }

    @Test
    void shouldReturnEmptyOptionalWhenCardNotFound() {
        Optional<Card> result = store.findById("unknown");
        assertThat(result).isEmpty();
    }

    @Test
    void shouldDeleteCardById() {
        Card card = new Card();
        card.setId("999");
        store.save(card);

        store.delete("999");

        assertThat(store.findById("999")).isEmpty();
    }

    @Test
    void shouldDoNothingWhenDeletingNonExistentCard() {
        store.delete("non-existent-id");
        assertThat(store.findAll()).isEmpty();
    }
}
