package com.example.quizz.web;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.quizz.domain.Answer;
import com.example.quizz.domain.AnswerRepository;
import com.example.quizz.domain.Category;
import com.example.quizz.domain.CategoryRepository;
import com.example.quizz.domain.Question;
import com.example.quizz.domain.QuestionRepository;
import com.example.quizz.domain.Quiz;
import com.example.quizz.domain.QuizRepository;
import com.example.quizz.domain.StudentAnswer;
import com.example.quizz.domain.StudentAnswerRepository;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import com.example.quizz.dto.CreateAnswerOptionDTO;


@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class ApiController {

    @Autowired
    private QuizRepository quizRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private AnswerRepository answerRepository;
    @Autowired
    private StudentAnswerRepository studentAnswerRepository;

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

    @GetMapping("/quizzes/{id}")
    @ResponseBody
    public ResponseEntity<Quiz> getQuizById(@PathVariable Long id) {
        return quizRepository.findById(id)
                .map(quiz -> new ResponseEntity<>(quiz, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/quizzes/{id}/questions")
    @ResponseBody
    public ResponseEntity<List<Question>> getQuizQuestions(@PathVariable Long id) {
        return quizRepository.findById(id)
                .map(quiz -> new ResponseEntity<>(quiz.getQuestions(), HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    //Get category by Id
    @GetMapping("/categories/{id}")
    @ResponseBody
    public Optional<Category> getCategoryById(@PathVariable Long id) {
        return categoryRepository.findById(id); 
    }

    //Get quizzes of a category
    //Work for now but require error messages logging 
    @GetMapping("/categories/{id}/quizzes")
    @ResponseBody
    public ResponseEntity<List<Quiz>> getQuizByCategoryId(@PathVariable Long id) {
        return categoryRepository.findById(id)
                .map(category -> new ResponseEntity<>(category.getQuizzes(), HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    
    // Submit answer for a question
    @PostMapping("questions/{questionId}/answers")
    public ResponseEntity<String> submitAnswer(
        @PathVariable Long questionId, 
        @RequestBody @Validated CreateAnswerOptionDTO dto) {
        
        // Validate the question exists
        Optional<Question> questionOptional = questionRepository.findById(questionId);        
        if (questionOptional.isEmpty()) {
            return new ResponseEntity<>("Question not found",HttpStatus.NOT_FOUND);
        }
        Question question = questionOptional.get();

        // Validate the quiz is published
        if (!question.getQuiz().isPublished()) {
            return new ResponseEntity<>("Quiz is not published", HttpStatus.BAD_REQUEST);
        }

        // Validate the answer option ID is provided and exists
        if(dto.getAnswerOptionId() == null) {
            return new ResponseEntity<>("Answer option ID must be provided", HttpStatus.BAD_REQUEST);
        }

        // Validate the answer option exists
        Optional<Answer> answerOptionOptional = answerRepository.findById(dto.getAnswerOptionId());
        if (answerOptionOptional.isEmpty()) {
            return new ResponseEntity<>("Answer option not found", HttpStatus.NOT_FOUND);
        }

        Answer selectedAnswer = answerOptionOptional.get();

        // Validate that the answer belongs to the same question
        if (!selectedAnswer.getQuestion().getId().equals(questionId)) {
        return new ResponseEntity<>("Answer option does not belong to the question", HttpStatus.BAD_REQUEST);
        }

        StudentAnswer studentAnswer = new StudentAnswer();
        studentAnswer.setAnswer(selectedAnswer);
        studentAnswerRepository.save(studentAnswer);
        
        return new ResponseEntity<>(HttpStatus.CREATED);
    }   
}
