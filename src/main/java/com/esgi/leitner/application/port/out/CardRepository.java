package com.esgi.leitner.application.port.out;

import com.esgi.leitner.domain.model.Card;
import java.util.List;
import java.util.Optional;

public interface CardRepository {
    List<Card> findAll();
    List<Card> findByTags(List<String> tags);
    Optional<Card> findById(String cardId);
    Card save(Card card);
}
