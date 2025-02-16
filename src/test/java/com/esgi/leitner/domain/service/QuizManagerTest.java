package com.esgi.leitner.domain.service;

import com.esgi.leitner.application.port.in.CardService;
import com.esgi.leitner.domain.model.Card;
import com.esgi.leitner.domain.model.User;
import com.esgi.leitner.infrastructure.dto.QuizAnswerRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
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

    // Tests pour startQuiz

    @Test
    void startQuiz_shouldThrowException_whenUserNotFound() {
        String userId = "user1";
        when(userService.getUserById(userId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> quizManager.startQuiz(userId))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("User not found");
    }

    @Test
    void startQuiz_shouldThrowException_whenQuizAlreadyTakenToday() {
        String userId = "user1";
        User user = new User();
        user.setId(userId);
        user.setLastQuizDate(LocalDate.now());
        when(userService.getUserById(userId)).thenReturn(Optional.of(user));

        assertThatThrownBy(() -> quizManager.startQuiz(userId))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("You have already taken a quiz today");
    }

    @Test
    void startQuiz_shouldUpdateLastQuizDate_whenQuizNotTakenToday() {
        String userId = "user1";
        User user = new User();
        user.setId(userId);
        user.setLastQuizDate(LocalDate.of(2020, 1, 1)); // Date ancienne
        when(userService.getUserById(userId)).thenReturn(Optional.of(user));

        quizManager.startQuiz(userId);

        verify(userService).updateUser(user);
        assertThat(user.getLastQuizDate()).isEqualTo(LocalDate.now());
    }

    // Tests pour processAnswer

    @Test
    void processAnswer_shouldReturnNotFound_whenCardDoesNotExist() {
        String cardId = "card1";
        QuizAnswerRequest quizAnswerRequest = new QuizAnswerRequest("Answer", false);
        when(cardService.getCardById(cardId)).thenReturn(null);

        String result = quizManager.processAnswer(cardId, quizAnswerRequest);
        assertThat(result).isEqualTo("Card not found.");
    }

    @Test
    void processAnswer_shouldReturnNotDefined_whenCardAnswerIsNull() {
        String cardId = "card1";
        Card card = new Card();
        card.setId(cardId);
        card.setAnswer(null);
        when(cardService.getCardById(cardId)).thenReturn(card);

        QuizAnswerRequest quizAnswerRequest = new QuizAnswerRequest("Any answer", false);
        String result = quizManager.processAnswer(cardId, quizAnswerRequest);
        assertThat(result).isEqualTo("Card answer not defined.");
    }

    @Test
    void processAnswer_shouldReturnIncorrectMessage_whenAnswerIncorrectAndNotForced() {
        String cardId = "card1";
        Card card = new Card();
        card.setId(cardId);
        card.setAnswer("Correct Answer");
        when(cardService.getCardById(cardId)).thenReturn(card);

        QuizAnswerRequest quizAnswerRequest = new QuizAnswerRequest("Wrong Answer", false);
        String result = quizManager.processAnswer(cardId, quizAnswerRequest);

        assertThat(result).isEqualTo("Incorrect answer. The correct answer is: Correct Answer");
    }

    @Test
    void processAnswer_shouldAcceptAnswer_whenAnswerCorrect() {
        String cardId = "card1";
        Card card = new Card();
        card.setId(cardId);
        card.setAnswer("Correct Answer");
        when(cardService.getCardById(cardId)).thenReturn(card);

        QuizAnswerRequest quizAnswerRequest = new QuizAnswerRequest("Correct Answer", false);
        String result = quizManager.processAnswer(cardId, quizAnswerRequest);

        verify(cardService).answerCard(cardId, true);
        assertThat(result).isEqualTo("Answer accepted.");
    }

    @Test
    void processAnswer_shouldAcceptForcedAnswer_whenAnswerIncorrectButForced() {
        String cardId = "card1";
        Card card = new Card();
        card.setId(cardId);
        card.setAnswer("Correct Answer");
        when(cardService.getCardById(cardId)).thenReturn(card);

        QuizAnswerRequest quizAnswerRequest = new QuizAnswerRequest("Wrong Answer", true);
        String result = quizManager.processAnswer(cardId, quizAnswerRequest);

        verify(cardService).answerCard(cardId, true);
        assertThat(result).isEqualTo("Answer accepted.");
    }
}
