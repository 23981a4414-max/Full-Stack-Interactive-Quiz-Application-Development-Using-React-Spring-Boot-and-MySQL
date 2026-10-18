package com.quizapp.web;

import com.quizapp.dto.Dtos.*;
import com.quizapp.service.QuizService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/quiz")
@RequiredArgsConstructor
public class QuizController {
    private final QuizService service;

    @GetMapping("/questions")
    public List<QuestionDto> questions(@RequestParam(defaultValue = "5") int limit) {
        return service.randomQuestions(Math.min(limit, 50));
    }

    @PostMapping("/submit")
    public SubmitResponse submit(@Valid @RequestBody SubmitRequest req, Authentication auth) {
        return service.submit(auth.getName(), req.answers());
    }

    @GetMapping("/results")
    public List<ResultDto> history(Authentication auth) {
        return service.history(auth.getName());
    }
}
