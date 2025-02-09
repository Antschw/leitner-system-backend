package com.esgi.leitner.infrastructure.adapter.out.storage;

import com.esgi.leitner.domain.model.Card;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * In-memory storage for Cards.
 * <p>
 * This is a simple storage implementation used to simulate a database.
 * It is thread-safe and optimized for retrieval operations.
 */
@Repository
public class InMemoryCardStore {
    private final Map<String, Card> cards = new HashMap<>();

    /**
     * Retrieves all stored cards.
     *
     * @return an unmodifiable list of all cards
     */
    public synchronized List<Card> findAll() {
        return List.copyOf(cards.values());
    }

    /**
     * Retrieves a card by its ID.
     *
     * @param id the ID of the card
     * @return an optional containing the found card, or empty if not found
     */
    public synchronized Optional<Card> findById(String id) {
        return Optional.ofNullable(cards.get(id));
    }

    /**
     * Stores or updates a card.
     *
     * @param card the card to store
     */
    public synchronized void save(Card card) {
        cards.put(card.getId(), card);
    }

    /**
     * Deletes a card by ID.
     *
     * @param id the ID of the card to delete
     */
    public synchronized void delete(String id) {
        cards.remove(id);
    }
}
