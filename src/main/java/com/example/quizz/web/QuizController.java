package com.example.quizz.web;

import com.example.quizz.domain.Category;
import com.example.quizz.domain.CategoryRepository;
import com.example.quizz.domain.Quiz;
import com.example.quizz.domain.QuizRepository;

import org.springframework.ui.Model;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/quizzes")
public class QuizController {

    @Autowired
    private QuizRepository quizRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    @GetMapping("/showQuizzes")
    public String showQuizzesString(Model model) {
        List<Quiz> quizzes = quizRepository.findAll(); 
        model.addAttribute("quizzes", quizzes);
        return "quizzes";
    }

    @GetMapping("/addquiz")
    public String showAddQuizForm(Model model) {
        List<Category> categories = categoryRepository.findAll();
        model.addAttribute("categories", categories);
        return "addquiz";
    }

    @PostMapping
    public String createQuiz(@ModelAttribute Quiz quiz) {
        quizRepository.save(quiz);
        return "redirect:/quizzes/showQuizzes"; 
    }

    @GetMapping("/{id}")
    public String deleteQuiz(@PathVariable Long id, Model model) {
        quizRepository.deleteById(id);

        if (quizRepository.existsById(id)) {
            model.addAttribute("message", "Delete failed"); 
            model.addAttribute("alertType", "danger"); 
        } else {
            model.addAttribute("message", "Delete successful");
            model.addAttribute("alertType", "success"); 
        }

        return "redirect:../quizzes/showQuizzes";
    }

    @GetMapping("/editquiz/{id}")
    public String showEditQuizForm(@PathVariable Long id, Model model) {
        Quiz quiz = quizRepository.findById(id).orElse(null);
        model.addAttribute("quiz", quiz);
        return "editquiz"; 
    }

    @PostMapping("/editquiz/{id}")
    public String updateQuiz(@PathVariable Long id, @ModelAttribute Quiz quiz, Model model) {
        Quiz existingQuiz = quizRepository.findById(id).orElse(null);
        
        if (existingQuiz != null) {
            existingQuiz.setName(quiz.getName());
            existingQuiz.setDescription(quiz.getDescription());
            existingQuiz.setCourseCode(quiz.getCourseCode());
            existingQuiz.setPublished(quiz.isPublished());
            
            quizRepository.save(existingQuiz);

            model.addAttribute("message", "Quiz updated successfully");
            model.addAttribute("alertType", "success");
        } else {
            model.addAttribute("message", "Quiz not found");
            model.addAttribute("alertType", "danger");
        }

        return "redirect:/quizzes/showQuizzes";
    }
}