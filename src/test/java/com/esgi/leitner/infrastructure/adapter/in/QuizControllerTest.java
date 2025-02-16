package com.esgi.leitner.infrastructure.adapter.in;

import com.esgi.leitner.domain.model.Card;
import com.esgi.leitner.domain.model.Category;
import com.esgi.leitner.domain.service.QuizManager;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class QuizControllerTest {

    private MockMvc mockMvc;

    @Mock
    private QuizManager quizManager;

    @InjectMocks
    private QuizController quizController;

    private AutoCloseable closeable;


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
    void createQuiz_success() throws Exception {
        String userId = "user1";

        Card card1 = new Card();
        card1.setId("1");
        card1.setQuestion("Question 1");
        card1.setAnswer(null);
        card1.setCategory(Category.FIRST);
        card1.setTag("Tag1");

        Card card2 = new Card();
        card2.setId("2");
        card2.setQuestion("Question 2");
        card2.setAnswer(null);
        card2.setCategory(Category.FIRST);
        card2.setTag("Tag2");

        when(quizManager.createQuiz(userId))
                .thenReturn(List.of(card1, card2));

        mockMvc.perform(post("/quiz/create")
                        .param("userId", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("""
            [
              {
                "id": "1",
                "question": "Question 1",
                "answer": null,
                "category": "FIRST",
                "tag": "Tag1"
              },
              {
                "id": "2",
                "question": "Question 2",
                "answer": null,
                "category": "FIRST",
                "tag": "Tag2"
              }
            ]
            """, true));
    }


    @Test
    void createQuiz_failure_userAlreadyDidQuiz() throws Exception {
        String userId = "user1";

        doThrow(new RuntimeException("You have already taken a quiz today."))
                .when(quizManager).createQuiz(userId);

        mockMvc.perform(post("/quiz/create")
                        .param("userId", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(""));
    }

}
