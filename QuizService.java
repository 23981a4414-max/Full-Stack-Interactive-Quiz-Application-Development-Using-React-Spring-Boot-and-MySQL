package com.quizapp.service;

import com.quizapp.dto.Dtos.*;
import com.quizapp.model.Question;
import com.quizapp.model.QuizResult;
import com.quizapp.repo.QuestionRepository;
import com.quizapp.repo.QuizResultRepository;
import com.quizapp.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@Service
@RequiredArgsConstructor
public class QuizService {
    private final QuestionRepository questions;
    private final QuizResultRepository results;
    private final UserRepository users;

    public List<QuestionDto> randomQuestions(int limit) {
        List<Question> all = new ArrayList<>(questions.findAll());
        Collections.shuffle(all);
        return all.stream().limit(Math.max(1, limit)).map(QuestionDto::from).toList();
    }

    @Transactional
    public SubmitResponse submit(String username, Map<Long, Integer> answers) {
        List<Question> qs = questions.findAllById(answers.keySet());
        if (qs.isEmpty()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No valid answers submitted");

        int score = 0;
        List<ReviewItem> review = new ArrayList<>();
        for (Question q : qs) {
            int selected = answers.getOrDefault(q.getId(), -1);
            boolean correct = selected == q.getCorrectIndex();
            if (correct) score++;
            review.add(new ReviewItem(q.getId(), q.getText(), q.getOptions(), selected,
                    q.getCorrectIndex(), correct, q.getExplanation()));
        }

        QuizResult r = new QuizResult();
        r.setUser(users.findByUsername(username).orElseThrow());
        r.setScore(score);
        r.setTotal(qs.size());
        results.save(r);
        return new SubmitResponse(score, qs.size(), review);
    }

    public List<ResultDto> history(String username) {
        return results.findByUserUsernameOrderByTakenAtDesc(username).stream().map(ResultDto::from).toList();
    }
}
