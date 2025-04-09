package com.example.quizz.web;

import com.example.quizz.domain.Quiz;
import com.example.quizz.domain.QuizRepository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/quizzes")
public class QuizController {

    @Autowired
    private QuizRepository quizRepository;

    // Create a new quiz
    @PostMapping
    public Quiz createQuiz(@RequestBody Quiz quiz) {
        return quizRepository.save(quiz);
    }

    @GetMapping
    public List<Quiz> getQuiz() {
        return quizRepository.findAll();
    }

    @DeleteMapping("/quizzes/'{id}")
    public String deleteQuiz(@PathVariable Long id) {
        quizRepository.deleteById(id);

        if (quizRepository.existsById(id) == true) {
            return "Delete failed";
        } else {
            return "Delete succeed";
        }

    }

}