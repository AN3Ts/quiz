package com.example.quizz.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.quizz.domain.Category;
import com.example.quizz.domain.CategoryRepository;
import com.example.quizz.domain.Quiz;
import com.example.quizz.domain.QuizRepository;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins="*")
public class ApiController {

    @Autowired
    private QuizRepository quizRepository;
    @Autowired
    private CategoryRepository categoryRepository;
   
     // Return json of all quizzes here with the annottation @ResponseBody
    @GetMapping("/quizzes") 
    @ResponseBody
    public List<Quiz> getQuiz() {
        List<Quiz> allQuizzes = quizRepository.findAll();
        List<Quiz> publishedQuizzes = allQuizzes.stream().filter(Quiz::isPublished).toList();
        return publishedQuizzes;
    }

    @GetMapping("/categories")
    @ResponseBody
    public List<Category> getCategory() {
        return categoryRepository.findAll();
    }
    

}
