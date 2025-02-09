package com.esgi.leitner.application.service;

import com.esgi.leitner.application.port.out.CardRepository;
import com.esgi.leitner.domain.model.Card;
import com.esgi.leitner.domain.model.Category;
import com.esgi.leitner.domain.service.CardReviewScheduler;
import com.esgi.leitner.domain.service.CategoryProgression;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CardServiceImplTest {

    @Mock
    private CardRepository cardRepository;

    @Mock
    private CategoryProgression categoryProgression;

    @Mock
    private CardReviewScheduler cardReviewScheduler;

    @InjectMocks
    private CardServiceImpl cardService;
    private AutoCloseable closeable;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldReturnAllCardsWhenNoTagsOrEmptyListIsProvided() {
        List<Card> mockCards = List.of(new Card(), new Card());
        when(cardRepository.findAll()).thenReturn(mockCards);

        List<Card> resultWithNull = cardService.getAllCards(null);
        List<Card> resultWithEmptyList = cardService.getAllCards(Collections.emptyList());

        assertThat(resultWithNull).isEqualTo(mockCards);
        assertThat(resultWithEmptyList).isEqualTo(mockCards);
        verify(cardRepository, times(2)).findAll();
    }

    @Test
    void shouldHandleTagFilteringCorrectly() {
        List<Card> filteredCards = List.of(new Card());
        when(cardRepository.findByTags(List.of("tag1"))).thenReturn(filteredCards);
        when(cardRepository.findByTags(List.of("unknownTag"))).thenReturn(Collections.emptyList());

        List<Card> resultWithValidTag = cardService.getAllCards(List.of("tag1"));
        List<Card> resultWithInvalidTag = cardService.getAllCards(List.of("unknownTag"));

        assertThat(resultWithValidTag).isEqualTo(filteredCards);
        assertThat(resultWithInvalidTag).isEmpty();

        verify(cardRepository).findByTags(List.of("tag1"));
        verify(cardRepository).findByTags(List.of("unknownTag"));
    }

    @Test
    void shouldReturnEmptyListWhenNoCardsExist() {
        when(cardRepository.findAll()).thenReturn(Collections.emptyList());

        List<Card> result = cardService.getQuizzCards(null);

        assertThat(result).isEmpty();
        verify(cardRepository).findAll();
        verify(cardReviewScheduler).selectCardsForReview(Collections.emptyList(), LocalDate.now());
    }

    @Test
    void shouldGenerateIdWhenCardIdIsNullOrBlank() {
        Card inputCard1 = new Card();
        Card inputCard2 = new Card();
        inputCard2.setId("");

        cardService.createCard(inputCard1);
        cardService.createCard(inputCard2);

        verify(cardRepository, times(2)).save(any(Card.class));
    }

    @Test
    void shouldSaveCardCorrectly() {
        Card card = new Card();
        card.setId("test-id");
        card.setQuestion("What is SOLID?");
        card.setAnswer("A set of design principles.");
        card.setTag("Best Practices");

        when(cardRepository.save(card)).thenReturn(card);

        Card savedCard = cardService.createCard(card);

        assertThat(savedCard).isNotNull();
        assertThat(savedCard.getId()).isEqualTo("test-id");
        verify(cardRepository).save(card);
    }

    @Test
    void shouldUpdateCardCategoryOnAnswer() {
        Card mockCard = new Card();
        mockCard.setCategory(Category.SECOND);
        when(cardRepository.findById("card1")).thenReturn(Optional.of(mockCard));
        when(categoryProgression.getNextCategory(Category.SECOND, true)).thenReturn(Category.THIRD);

        cardService.answerCard("card1", true);

        ArgumentCaptor<Card> captor = ArgumentCaptor.forClass(Card.class);
        verify(cardRepository).save(captor.capture());
        assertThat(captor.getValue().getCategory()).isEqualTo(Category.THIRD);
    }

    @Test
    void shouldParseDateWhenProvided() {
        List<Card> mockCards = List.of(new Card(), new Card());
        when(cardRepository.findAll()).thenReturn(mockCards);

        LocalDate providedDate = LocalDate.of(2024, 2, 9);
        when(cardReviewScheduler.selectCardsForReview(mockCards, providedDate)).thenReturn(mockCards);

        List<Card> result = cardService.getQuizzCards("2024-02-09");

        assertThat(result).isEqualTo(mockCards);
        verify(cardRepository).findAll();
        verify(cardReviewScheduler).selectCardsForReview(mockCards, providedDate);
    }

    @Test
    void shouldNotUpdateIfCardNotFound() {
        when(cardRepository.findById("unknown")).thenReturn(Optional.empty());

        cardService.answerCard("unknown", true);

        verify(cardRepository, never()).save(any());
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }
}
