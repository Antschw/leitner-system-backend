package com.esgi.leitner.infrastructure.adapter.in;

import com.esgi.leitner.application.port.in.CardService;
import com.esgi.leitner.domain.model.Card;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class CardControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CardService cardService;

    @InjectMocks
    private CardController cardController;
    private AutoCloseable closeable;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(cardController).build();
    }

    @Test
    void shouldReturnAllCards() throws Exception {
        List<Card> mockCards = List.of(new Card(), new Card());
        when(cardService.getAllCards(null)).thenReturn(mockCards);

        mockMvc.perform(get("/cards"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(mockCards.size()));

        verify(cardService).getAllCards(null);
    }

    @Test
    void shouldReturnFilteredCardsByTags() throws Exception {
        List<Card> mockCards = List.of(new Card());
        when(cardService.getAllCards(List.of("tag1"))).thenReturn(mockCards);

        mockMvc.perform(get("/cards").param("tags", "tag1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(mockCards.size()));

        verify(cardService).getAllCards(List.of("tag1"));
    }

    @Test
    void shouldCreateCard() throws Exception {
        Card newCard = new Card();
        newCard.setQuestion("What is TDD?");
        newCard.setAnswer("Test-Driven Development");
        newCard.setTag("Software Engineering");

        when(cardService.createCard(any(Card.class))).thenReturn(newCard);

        mockMvc.perform(post("/cards")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "question": "What is TDD?",
                                    "answer": "Test-Driven Development",
                                    "tag": "Software Engineering"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.question").value("What is TDD?"))
                .andExpect(jsonPath("$.answer").value("Test-Driven Development"))
                .andExpect(jsonPath("$.tag").value("Software Engineering"));

        verify(cardService).createCard(any(Card.class));
    }

    @Test
    void shouldReturnQuizzCards() throws Exception {
        List<Card> mockCards = List.of(new Card(), new Card());
        when(cardService.getQuizzCards(null)).thenReturn(mockCards);

        mockMvc.perform(get("/cards/quizz"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(mockCards.size()));

        verify(cardService).getQuizzCards(null);
    }

    @Test
    void shouldReturnQuizzCardsForGivenDate() throws Exception {
        List<Card> mockCards = List.of(new Card());
        when(cardService.getQuizzCards("2024-02-09")).thenReturn(mockCards);

        mockMvc.perform(get("/cards/quizz").param("date", "2024-02-09"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(mockCards.size()));

        verify(cardService).getQuizzCards("2024-02-09");
    }

    @Test
    void shouldAnswerCard() throws Exception {
        mockMvc.perform(patch("/cards/{cardId}/answer", "123")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                                "isValid": true
                            }
                            """))
                .andExpect(status().isOk());

        verify(cardService).answerCard("123", true);
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }
}
