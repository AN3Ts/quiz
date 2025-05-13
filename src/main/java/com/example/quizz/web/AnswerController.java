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

    @GetMapping("/question/{questionId}")
    public String showAnswers(@PathVariable Long questionId, Model model) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid question ID: " + questionId));
        model.addAttribute("questionText", question.getQuestionText());
        model.addAttribute("questionId", question.getId());
        model.addAttribute("answers", question.getAnswers());
        model.addAttribute("quizId", question.getQuiz().getId());

        return "answers";
    }

    @GetMapping("/add/{questionId}")
    public String showAddAnswerForm(@PathVariable Long questionId, Model model) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid question ID: " + questionId));
        model.addAttribute("questionText", question.getQuestionText());
        model.addAttribute("questionId", question.getId());
        model.addAttribute("quizId", question.getQuiz().getId());

        return "addanswer";
    }

    @PostMapping
    public String createAnswer(
            @RequestParam Long questionId,
            @RequestParam String answerText,
            @RequestParam(required = false, defaultValue = "false") boolean isCorrect) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid question ID: " + questionId));

        Answer answer = new Answer();
        answer.setAnswerText(answerText);
        answer.setIsCorrect(isCorrect);
        answer.setQuestion(question);

        answerRepository.save(answer);

        return "redirect:/answers/question/" + questionId;
    }

    @GetMapping("/delete/{questionId}/{answerId}")
    public String deleteAnswer(@PathVariable Long questionId, @PathVariable Long answerId, Model model) {
        answerRepository.deleteById(answerId);

        if (answerRepository.existsById(answerId)) {
            model.addAttribute("message", "Delete failed");
            model.addAttribute("alertType", "danger");
        } else {
            model.addAttribute("message", "Delete successful");
            model.addAttribute("alertType", "success");
        }

        return "redirect:/answers/question/" + questionId;
    }
}
