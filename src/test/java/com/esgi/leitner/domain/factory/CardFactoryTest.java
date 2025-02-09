package com.esgi.leitner.domain.factory;

import com.esgi.leitner.domain.model.Card;
import com.esgi.leitner.domain.model.Category;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CardFactoryTest {

    @Test
    void shouldGenerateNewCardWithUniqueId() {
        Card card1 = CardFactory.createCard("What is Dependency Injection?", "A design pattern for managing dependencies.", "Design Patterns");
        Card card2 = CardFactory.createCard("What is Clean Architecture?", "A layered approach to software design.", "Architecture");

        assertThat(card1.getId()).isNotNull();
        assertThat(card2.getId()).isNotNull();
        assertThat(card1.getId()).isNotEqualTo(card2.getId());
    }

    @Test
    void shouldAssignFirstCategoryToNewCard() {
        Card card = CardFactory.createCard("Explain SOLID principles.", "A set of five design principles.", "Software Engineering");

        assertThat(card.getCategory()).isEqualTo(Category.FIRST);
    }
}
