package com.quizapp.dto;

import com.quizapp.model.Question;
import com.quizapp.model.QuizResult;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public final class Dtos {
    private Dtos() {}

    public record RegisterRequest(
            @NotBlank @Size(min = 3, max = 30) String username,
            @NotBlank @Size(min = 6, max = 100) String password) {}

    public record LoginRequest(@NotBlank String username, @NotBlank String password) {}

    public record AuthResponse(String token, String username, String role) {}

    /** What quiz takers see: NO correct answer. */
    public record QuestionDto(Long id, String text, List<String> options, String category) {
        public static QuestionDto from(Question q) {
            return new QuestionDto(q.getId(), q.getText(), q.getOptions(), q.getCategory());
        }
    }

    /** What admins see: includes the correct answer. */
    public record QuestionAdminDto(Long id, String text, List<String> options, int correctIndex,
                                   String category, String explanation) {
        public static QuestionAdminDto from(Question q) {
            return new QuestionAdminDto(q.getId(), q.getText(), q.getOptions(), q.getCorrectIndex(),
                    q.getCategory(), q.getExplanation());
        }
    }

    public record QuestionRequest(
            @NotBlank @Size(max = 500) String text,
            @NotNull @Size(min = 2, max = 6) List<@NotBlank String> options,
            @Min(0) int correctIndex,
            @Size(max = 255) String category,
            @Size(max = 500) String explanation) {}

    /** questionId -> selected option index (-1 = skipped) */
    public record SubmitRequest(@NotNull @NotEmpty Map<Long, Integer> answers) {}

    public record ReviewItem(Long questionId, String text, List<String> options, int selectedIndex,
                             int correctIndex, boolean correct, String explanation) {}

    public record SubmitResponse(int score, int total, List<ReviewItem> review) {}

    public record ResultDto(Long id, int score, int total, LocalDateTime takenAt) {
        public static ResultDto from(QuizResult r) {
            return new ResultDto(r.getId(), r.getScore(), r.getTotal(), r.getTakenAt());
        }
    }
}
