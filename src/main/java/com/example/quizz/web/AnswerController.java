package com.example.quizz.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.quizz.domain.Answer;
import com.example.quizz.domain.AnswerRepository;
import com.example.quizz.domain.Question;
import com.example.quizz.domain.QuestionRepository;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/answers")
public class AnswerController {
    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private QuestionRepository questionRepository;

    // Show all answers of a question
    @GetMapping("/question/{questionId}")
    public String showAnswers(@PathVariable Long questionId, Model model) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid question ID: " + questionId));
        model.addAttribute("questionText", question.getQuestionText());
        model.addAttribute("questionId", question.getId());
        model.addAttribute("answers", question.getAnswers());

        return "answers";

    }

}
