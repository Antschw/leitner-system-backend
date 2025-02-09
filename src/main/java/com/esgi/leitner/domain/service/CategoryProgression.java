package com.esgi.leitner.domain.service;

import com.esgi.leitner.domain.model.Category;
import org.springframework.stereotype.Component;

/**
 * Handles category progression for cards in the Leitner system.
 */
@Component
public class CategoryProgression {

    /**
     * Determines the next category of a card based on the user's answer.
     *
     * @param currentCategory The current category of the card.
     * @param isValid Whether the user's answer was correct.
     * @return The new category of the card.
     */
    public Category getNextCategory(Category currentCategory, boolean isValid) {
        if (!isValid) {
            return Category.FIRST;
        }
        return (currentCategory == Category.DONE) ? Category.DONE :
                Category.values()[currentCategory.ordinal() + 1];
    }
}
