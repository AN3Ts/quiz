package com.example.quizz.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.quizz.domain.Question;
import com.example.quizz.domain.QuestionRepository;
import com.example.quizz.domain.Quiz;
import com.example.quizz.domain.QuizRepository;

@Controller
@RequestMapping("/questions")
public class QuestionController {

    @Autowired
    private QuizRepository quizRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @GetMapping("/quiz/{quizId}")
    public String showQuestions(@PathVariable Long quizId, Model model) {
        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid quiz ID: " + quizId));
        model.addAttribute("quizName", quiz.getName());
        model.addAttribute("quizId", quiz.getId()); 
        model.addAttribute("questions", quiz.getQuestions()); 

        return "questions";
    }
    
    @GetMapping("/add/{quizId}")
    public String showAddQuestionForm(@PathVariable Long quizId, Model model) {
        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid quiz ID: " + quizId));
        model.addAttribute("quizName", quiz.getName());
        model.addAttribute("quizId", quiz.getId());
        return "addquestion";
    }

    
    @PostMapping
    public String createQuestion(@RequestParam Long quizId, @RequestParam String questionText,
            @RequestParam String difficulty) {
        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid quiz ID: " + quizId));

        Question question = new Question();
        question.setQuestionText(questionText);
        question.setDifficulty(Question.Difficulty.valueOf(difficulty));
        question.setQuiz(quiz);

        questionRepository.save(question);

        return "redirect:/questions/quiz/" + quizId;
    }

    @GetMapping("/delete/{quizId}/{questionId}")
    public String deleteQuestion(@PathVariable Long quizId, @PathVariable Long questionId, Model model) {
        questionRepository.deleteById(questionId);

        if (questionRepository.existsById(questionId)) {
            model.addAttribute("message", "Delete failed"); 
            model.addAttribute("alertType", "danger"); 
        } else {
            model.addAttribute("message", "Delete successful");
            model.addAttribute("alertType", "success"); 
        }

        return "redirect:/questions/quiz/" + quizId;
    }
}
