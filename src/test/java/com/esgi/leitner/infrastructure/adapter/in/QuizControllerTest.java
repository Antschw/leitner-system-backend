package com.esgi.leitner.infrastructure.adapter.in;

import com.esgi.leitner.domain.service.QuizManager;
import com.esgi.leitner.infrastructure.dto.QuizAnswerRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class QuizControllerTest {

    private MockMvc mockMvc;

    @Mock
    private QuizManager quizManager;

    @InjectMocks
    private QuizController quizController;

    private AutoCloseable closeable;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(quizController).build();
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    void startQuiz_success() throws Exception {
        String userId = "user1";
        // Aucune exception levée
        mockMvc.perform(post("/quiz/start")
                        .param("userId", userId))
                .andExpect(status().isOk())
                .andExpect(content().string("Quiz started successfully."));
    }

    @Test
    void startQuiz_failure() throws Exception {
        String userId = "user1";
        doThrow(new RuntimeException("You have already taken a quiz today."))
                .when(quizManager).startQuiz(userId);

        mockMvc.perform(post("/quiz/start")
                        .param("userId", userId))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("You have already taken a quiz today."));
    }

    @Test
    void answerQuestion_success() throws Exception {
        String cardId = "card1";
        QuizAnswerRequest quizAnswerRequest = new QuizAnswerRequest("Correct Answer", false);
        when(quizManager.processAnswer(Mockito.eq(cardId), Mockito.any(QuizAnswerRequest.class)))
                .thenReturn("Answer accepted.");


        mockMvc.perform(patch("/quiz/{cardId}/answer", cardId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(quizAnswerRequest)))
                .andExpect(status().isOk())
                .andExpect(content().string("Answer accepted."));
    }

    @Test
    void answerQuestion_incorrect() throws Exception {
        String cardId = "card1";
        QuizAnswerRequest quizAnswerRequest = new QuizAnswerRequest("Wrong Answer", false);
        when(quizManager.processAnswer(Mockito.eq(cardId), Mockito.any(QuizAnswerRequest.class)))
                .thenReturn("Incorrect answer. The correct answer is: Correct Answer");

        mockMvc.perform(patch("/quiz/{cardId}/answer", cardId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(quizAnswerRequest)))
                .andExpect(status().isOk())
                .andExpect(content().string("Incorrect answer. The correct answer is: Correct Answer"));
    }
}
