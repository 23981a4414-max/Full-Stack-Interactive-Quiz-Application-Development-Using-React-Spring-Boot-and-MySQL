package com.quizapp;

import com.quizapp.dto.Dtos.SubmitResponse;
import com.quizapp.model.Question;
import com.quizapp.model.User;
import com.quizapp.repo.QuestionRepository;
import com.quizapp.repo.QuizResultRepository;
import com.quizapp.repo.UserRepository;
import com.quizapp.service.QuizService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class QuizServiceTest {

    private Question question(long id, int correct) {
        Question q = new Question();
        q.setId(id);
        q.setText("Q" + id);
        q.setOptions(List.of("a", "b", "c"));
        q.setCorrectIndex(correct);
        return q;
    }

    @Test
    void scoresCorrectAnswersAndTreatsSkippedAsWrong() {
        var qr = Mockito.mock(QuestionRepository.class);
        var rr = Mockito.mock(QuizResultRepository.class);
        var ur = Mockito.mock(UserRepository.class);
        when(qr.findAllById(any())).thenReturn(List.of(question(1, 0), question(2, 2), question(3, 1)));
        when(ur.findByUsername("bob")).thenReturn(Optional.of(new User()));

        SubmitResponse res = new QuizService(qr, rr, ur).submit("bob", Map.of(1L, 0, 2L, 1, 3L, -1));

        assertEquals(1, res.score());
        assertEquals(3, res.total());
        assertFalse(res.review().get(1).correct());
    }
}
