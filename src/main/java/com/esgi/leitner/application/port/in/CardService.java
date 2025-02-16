package com.esgi.leitner.application.port.in;

import com.esgi.leitner.domain.model.Card;
import com.esgi.leitner.infrastructure.dto.AnswerRequest;

import java.util.List;

public interface CardService {
    List<Card> getAllCards(List<String> tags);
    Card createCard(Card card);
    List<Card> getQuizzCards(String date);
    void answerCard(String cardId, boolean isValid);
    Card getCardById(String cardId);
}
