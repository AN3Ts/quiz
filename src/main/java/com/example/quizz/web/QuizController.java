package com.example.quizz.web;

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

    // Return all quizzes
    @GetMapping("/showQuizzes")
    public String showQuizzesString(Model model) {
        List<Quiz> quizzes = quizRepository.findAll(); 
        model.addAttribute("quizzes", quizzes);
        return "quizzes";
    }

    // Show the form to add a new quiz
    @GetMapping("/addquiz")
    public String showAddQuizForm() {
        return "addquiz";
    }

    // Create and save a new quiz
    @PostMapping
    public String createQuiz(@ModelAttribute Quiz quiz) {
        quizRepository.save(quiz);
        return "redirect:/quizzes/showQuizzes"; 
    }

    // Return json of all quizzes here with the annottation @ResponseBody
    @GetMapping
    @ResponseBody
    public List<Quiz> getQuiz() {
        return quizRepository.findAll();
    }

    // Delete a quiz by its id
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

    // Get the quiz to be edited and show the edit form
    @GetMapping("/editquiz/{id}")
    public String showEditQuizForm(@PathVariable Long id, Model model) {
        Quiz quiz = quizRepository.findById(id).orElse(null);
        model.addAttribute("quiz", quiz);
        return "editquiz"; 
    }

    // Update the quiz with the new data from the edit form
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