package com.esgi.leitner.domain.service;

import com.esgi.leitner.domain.model.Category;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CategoryProgressionTest {

    private CategoryProgression categoryProgression;

    @BeforeEach
    void setUp() {
        categoryProgression = new CategoryProgression();
    }

    @Test
    void shouldResetToFirstCategoryWhenAnswerIsIncorrect() {
        Category nextCategory = categoryProgression.getNextCategory(Category.THIRD, false);
        assertThat(nextCategory).isEqualTo(Category.FIRST);
    }

    @Test
    void shouldMoveToNextCategoryWhenAnswerIsCorrect() {
        Category nextCategory = categoryProgression.getNextCategory(Category.FIRST, true);
        assertThat(nextCategory).isEqualTo(Category.SECOND);
    }

    @Test
    void shouldNotGoBeyondDoneCategory() {
        Category nextCategory = categoryProgression.getNextCategory(Category.DONE, true);
        assertThat(nextCategory).isEqualTo(Category.DONE);
    }

    @Test
    void shouldMoveThroughAllCategoriesCorrectly() {
        Category category = Category.FIRST;
        for (int i = 1; i < Category.values().length - 1; i++) {
            category = categoryProgression.getNextCategory(category, true);
            assertThat(category).isEqualTo(Category.values()[i]);
        }
    }
}
