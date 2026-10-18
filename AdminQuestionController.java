package com.quizapp.web;

import com.quizapp.dto.Dtos.*;
import com.quizapp.model.Question;
import com.quizapp.repo.QuestionRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/admin/questions")
@RequiredArgsConstructor
public class AdminQuestionController {
    private final QuestionRepository repo;

    @GetMapping
    public List<QuestionAdminDto> all() {
        return repo.findAll().stream().map(QuestionAdminDto::from).toList();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public QuestionAdminDto create(@Valid @RequestBody QuestionRequest req) {
        return QuestionAdminDto.from(repo.save(apply(new Question(), req)));
    }

    @PutMapping("/{id}")
    public QuestionAdminDto update(@PathVariable Long id, @Valid @RequestBody QuestionRequest req) {
        Question q = repo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return QuestionAdminDto.from(repo.save(apply(q, req)));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        if (!repo.existsById(id)) throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        repo.deleteById(id);
    }

    private Question apply(Question q, QuestionRequest r) {
        if (r.correctIndex() >= r.options().size())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "correctIndex out of range");
        q.setText(r.text());
        q.setOptions(new ArrayList<>(r.options()));
        q.setCorrectIndex(r.correctIndex());
        q.setCategory(r.category());
        q.setExplanation(r.explanation());
        return q;
    }
}
