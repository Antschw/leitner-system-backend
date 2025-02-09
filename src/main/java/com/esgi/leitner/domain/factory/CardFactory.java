package com.esgi.leitner.domain.factory;

import com.esgi.leitner.domain.model.Card;
import com.esgi.leitner.domain.model.Category;

import java.util.UUID;

public class CardFactory {
    public static Card createCard(String question, String answer, String tag) {
        Card card = new Card();
        card.setId(UUID.randomUUID().toString());
        card.setQuestion(question);
        card.setAnswer(answer);
        card.setTag(tag);
        card.setCategory(Category.FIRST);
        return card;
    }
}
