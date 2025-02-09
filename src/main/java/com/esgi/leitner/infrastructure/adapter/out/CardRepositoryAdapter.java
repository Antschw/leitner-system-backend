package com.esgi.leitner.infrastructure.adapter.out;

import com.esgi.leitner.application.port.out.CardRepository;
import com.esgi.leitner.domain.model.Card;
import com.esgi.leitner.infrastructure.adapter.out.storage.InMemoryCardStore;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Adapter class implementing CardRepository using an in-memory storage.
 */
@Repository
@RequiredArgsConstructor
public class CardRepositoryAdapter implements CardRepository {
    private final InMemoryCardStore storage;

    /**
     * Retrieves all stored cards.
     *
     * @return a list of all stored cards
     */
    @Override
    public List<Card> findAll() {
        return storage.findAll();
    }

    /**
     * Finds cards matching the provided tags.
     *
     * @param tags the list of tags to filter by
     * @return a list of matching cards
     */
    @Override
    public List<Card> findByTags(List<String> tags) {
        return (tags == null || tags.isEmpty()) ? findAll() :
                storage.findAll().stream()
                        .filter(card -> tags.contains(card.getTag()))
                        .toList();
    }

    /**
     * Retrieves a card by its ID.
     *
     * @param cardId the ID of the card
     * @return an optional containing the found card, or empty if not found
     */
    @Override
    public Optional<Card> findById(String cardId) {
        return storage.findById(cardId);
    }

    /**
     * Saves a card.
     *
     * @param card the card to save
     * @return the saved card
     */
    @Override
    public Card save(Card card) {
        storage.save(card);
        return card;
    }

    /**
     * Deletes a card by ID.
     *
     * @param cardId the ID of the card to delete
     */
    public void delete(String cardId) {
        storage.delete(cardId);
    }
}
