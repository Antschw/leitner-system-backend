package com.esgi.leitner.domain.service;

import com.esgi.leitner.application.port.in.CardService;
import com.esgi.leitner.domain.model.Card;
import com.esgi.leitner.domain.model.Category;
import com.esgi.leitner.domain.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class QuizManagerTest {

    @Mock
    private UserService userService;

    @Mock
    private CardService cardService;

    @InjectMocks
    private QuizManager quizManager;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createQuiz_shouldThrowExceptionIfUserNotFound() {
        String userId = "user1";
        when(userService.getUserById(userId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> quizManager.createQuiz(userId))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("User not found");
    }

    @Test
    void createQuiz_shouldThrowExceptionIfQuizAlreadyTakenToday() {
        String userId = "user1";
        User user = new User();
        user.setId(userId);
        user.setLastQuizDate(LocalDate.now());

        when(userService.getUserById(userId)).thenReturn(Optional.of(user));

        assertThatThrownBy(() -> quizManager.createQuiz(userId))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("You have already taken a quiz today.");
    }

    @Test
    void createQuiz_shouldUpdateLastQuizDateWhenNotTakenToday() {
        String userId = "user1";
        User user = new User();
        user.setId(userId);
        user.setLastQuizDate(LocalDate.of(2020, 1, 1));
        when(userService.getUserById(userId)).thenReturn(Optional.of(user));

        Card card1 = new Card();
        card1.setId("1");
        card1.setAnswer("ANSWER_1");
        card1.setCategory(Category.FIRST);

        Card card2 = new Card();
        card2.setId("2");
        card2.setAnswer("ANSWER_2");
        card2.setCategory(Category.DONE);

        when(cardService.getQuizzCards(null)).thenReturn(List.of(card1, card2));

        List<Card> result = quizManager.createQuiz(userId);

        verify(userService).updateUser(user);
        assertThat(user.getLastQuizDate()).isEqualTo(LocalDate.now());

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getId()).isEqualTo("1");
        assertThat(result.get(0).getAnswer()).isNull();

        verify(cardService).getQuizzCards(null);
    }
    @Test
    void createQuiz_shouldFilterDoneCardsAndRemoveAnswers() {
        String userId = "user1";
        User user = new User();
        user.setId(userId);
        user.setLastQuizDate(LocalDate.of(2020, 1, 1));
        when(userService.getUserById(userId)).thenReturn(Optional.of(user));

        Card card1 = new Card();
        card1.setId("1");
        card1.setAnswer("ANSWER_1");
        card1.setCategory(Category.FIRST);

        Card card2 = new Card();
        card2.setId("2");
        card2.setAnswer("ANSWER_2");
        card2.setCategory(Category.DONE);

        when(cardService.getQuizzCards(null)).thenReturn(List.of(card1, card2));

        List<Card> filteredCards = quizManager.createQuiz(userId);

        assertThat(filteredCards).hasSize(1);
        Card returnedCard = filteredCards.get(0);
        assertThat(returnedCard.getId()).isEqualTo("1");
        assertThat(returnedCard.getAnswer()).isNull();
        assertThat(returnedCard.getCategory()).isEqualTo(Category.FIRST);
    }


}
