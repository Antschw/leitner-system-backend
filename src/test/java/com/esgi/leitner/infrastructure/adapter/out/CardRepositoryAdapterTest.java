package com.esgi.leitner.infrastructure.adapter.out;

import com.esgi.leitner.domain.model.Card;
import com.esgi.leitner.domain.model.Category;
import com.esgi.leitner.infrastructure.adapter.out.storage.InMemoryCardStore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class CardRepositoryAdapterTest {

    private CardRepositoryAdapter repository;

    @BeforeEach
    void setUp() {
        InMemoryCardStore store = new InMemoryCardStore();
        repository = new CardRepositoryAdapter(store);
    }

    @Test
    void shouldReturnEmptyListWhenNoCardsExist() {
        List<Card> result = repository.findAll();
        assertThat(result).isEmpty();
    }

    @Test
    void shouldSaveAndRetrieveCard() {
        Card card = new Card();
        card.setId("1");
        card.setQuestion("What is DDD?");
        card.setAnswer("Domain-Driven Design");
        card.setTag("Architecture");
        card.setCategory(Category.FIRST);

        repository.save(card);
        Optional<Card> retrieved = repository.findById("1");

        assertThat(retrieved).isPresent();
        assertThat(retrieved.get()).isEqualTo(card);
    }

    @Test
    void shouldReturnAllStoredCards() {
        Card card1 = new Card();
        card1.setId("1");
        repository.save(card1);

        Card card2 = new Card();
        card2.setId("2");
        repository.save(card2);

        List<Card> allCards = repository.findAll();
        assertThat(allCards).hasSize(2).containsExactlyInAnyOrder(card1, card2);
    }

    @Test
    void shouldReturnEmptyOptionalWhenCardNotFound() {
        Optional<Card> result = repository.findById("unknown");
        assertThat(result).isEmpty();
    }

    @Test
    void shouldDeleteCardById() {
        Card card = new Card();
        card.setId("777");
        repository.save(card);

        repository.delete("777");

        assertThat(repository.findById("777")).isEmpty();
    }

    @Test
    void shouldReturnCardsMatchingTags() {
        Card card1 = new Card();
        card1.setId("1");
        card1.setTag("Java");
        repository.save(card1);

        Card card2 = new Card();
        card2.setId("2");
        card2.setTag("Spring");
        repository.save(card2);

        List<Card> result = repository.findByTags(List.of("Java"));
        assertThat(result).containsExactly(card1);
    }

    @Test
    void shouldReturnEmptyListWhenTagsDoNotMatch() {
        Card card = new Card();
        card.setId("1");
        card.setTag("Python");
        repository.save(card);

        List<Card> result = repository.findByTags(List.of("Java"));
        assertThat(result).isEmpty();
    }

    @Test
    void shouldReturnAllCardsWhenTagsAreEmpty() {
        Card card1 = new Card();
        card1.setId("1");
        repository.save(card1);

        Card card2 = new Card();
        card2.setId("2");
        repository.save(card2);

        List<Card> result = repository.findByTags(List.of());
        assertThat(result).containsExactlyInAnyOrder(card1, card2);
    }

    @Test
    void shouldReturnAllCardsWhenTagsAreNull() {
        Card card1 = new Card();
        card1.setId("1");
        repository.save(card1);

        Card card2 = new Card();
        card2.setId("2");
        repository.save(card2);

        List<Card> result = repository.findByTags(null);
        assertThat(result).containsExactlyInAnyOrder(card1, card2);
    }
}
