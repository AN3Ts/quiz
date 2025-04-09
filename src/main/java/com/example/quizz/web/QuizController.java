package com.example.quizz.web;

import com.example.quizz.domain.Quiz;
import com.example.quizz.domain.QuizRepository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
@RequestMapping("/quizzes")
public class QuizController {

    @Autowired
    private QuizRepository quizRepository;

    // Serve the Add Quiz form
    @GetMapping("/addquiz")
    public String showAddQuizForm() {
        return "addquiz";
    }

    // Create a new quiz
    @PostMapping
    public Quiz createQuiz(@RequestBody Quiz quiz) {
        return quizRepository.save(quiz);
    }

    @GetMapping
    public List<Quiz> getQuiz() {
        return quizRepository.findAll();
    }
    

}