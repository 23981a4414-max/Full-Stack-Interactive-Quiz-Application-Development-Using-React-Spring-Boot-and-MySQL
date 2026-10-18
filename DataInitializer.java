package com.quizapp.config;

import com.quizapp.model.Question;
import com.quizapp.model.User;
import com.quizapp.repo.QuestionRepository;
import com.quizapp.repo.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner seed(UserRepository users, QuestionRepository questions, PasswordEncoder encoder,
                           @Value("${app.admin.username}") String adminUser,
                           @Value("${app.admin.password}") String adminPass) {
        return args -> {
            if (!users.existsByUsername(adminUser)) {
                User u = new User();
                u.setUsername(adminUser);
                u.setPassword(encoder.encode(adminPass));
                u.setRole("ADMIN");
                users.save(u);
            }
            if (questions.count() == 0) {
                questions.saveAll(List.of(
                    q("Which keyword is used to inherit a class in Java?", List.of("implements", "extends", "inherits", "super"), 1, "Java", "'extends' creates a subclass."),
                    q("Which hook manages state in a React function component?", List.of("useEffect", "useRef", "useState", "useMemo"), 2, "React", "useState returns a value and a setter."),
                    q("Which annotation marks a Spring Boot REST controller?", List.of("@Controller", "@RestController", "@Service", "@Component"), 1, "Spring", "@RestController = @Controller + @ResponseBody."),
                    q("Which SQL clause filters rows after grouping?", List.of("WHERE", "GROUP BY", "HAVING", "ORDER BY"), 2, "MySQL", "HAVING filters aggregated groups."),
                    q("What does HTTP status 404 mean?", List.of("Server error", "Unauthorized", "Not Found", "Created"), 2, "Web", "The resource was not found."),
                    q("Which method adds an element to the end of a JS array?", List.of("push", "pop", "shift", "unshift"), 0, "JavaScript", "push appends to the end.")
                ));
            }
        };
    }

    private Question q(String text, List<String> opts, int correct, String cat, String expl) {
        Question q = new Question();
        q.setText(text);
        q.setOptions(new ArrayList<>(opts));
        q.setCorrectIndex(correct);
        q.setCategory(cat);
        q.setExplanation(expl);
        return q;
    }
}
