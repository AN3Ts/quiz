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

    // return all quizzes in UI view
    @GetMapping("/showQuizzes")
    public String showQuizzesString(Model model) {
        List<Quiz> quizzes = quizRepository.findAll(); 
        model.addAttribute("quizzes", quizzes);
        return "quizzes"; //return quizzes.html
    }

    // Serve the Add Quiz form
    @GetMapping("/addquiz")
    public String showAddQuizForm() {
        return "addquiz"; //return addquizz.html
    }

    // Create and save new quiz
    @PostMapping
    public String createQuiz(@ModelAttribute Quiz quiz) {
        quizRepository.save(quiz);
        return "redirect:/quizzes"; 
    } //successful save currentlt return json of newly added quiz, and not the homepage

    // return json of all quizzes here with the annottation @ResponseBody
    @GetMapping
    @ResponseBody
    public List<Quiz> getQuiz() {
        return quizRepository.findAll();
    }

    @GetMapping("/{id}") //This has to be a GetMapping, not DeleteMapping, method used below is deleteById
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
}