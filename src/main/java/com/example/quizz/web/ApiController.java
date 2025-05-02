package com.example.quizz.web;

import java.util.ArrayList;
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
import com.example.quizz.dto.QuestionResultDTO;


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
    public ResponseEntity<?> getQuizById(@PathVariable Long id) {
       Optional<Quiz> quizOptional = quizRepository.findById(id); //the findById method returns an optional object
        if (quizOptional.isEmpty()){
            return new ResponseEntity<>("Quiz with the provided ID does not exist!",HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity<>(quizOptional.get(),HttpStatus.OK);
            //with the Optional object, use method get() to retrieve the actual object
        }
    }

    @GetMapping("/quizzes/{id}/questions")
    @ResponseBody
    public ResponseEntity<?> getQuizQuestions(@PathVariable Long id) {
        Optional<Quiz> quizOptional = quizRepository.findById(id); 

        if (quizOptional.isEmpty()){
            return new ResponseEntity<>("Quiz with the provided ID does not exist!",HttpStatus.BAD_REQUEST);
        }else {
            Quiz selectedQuiz = quizOptional.get();
            return new ResponseEntity<>(selectedQuiz.getQuestions(), HttpStatus.OK); 
        }
                
    }

    //Get category by Id
    @GetMapping("/categories/{id}")
    @ResponseBody
    public ResponseEntity<?> getCategoryById(@PathVariable Long id) {
        Optional<Category> catOptional = categoryRepository.findById(id); 

        if (catOptional.isEmpty()){
            return new ResponseEntity<>("Category with the provided ID does not exist", HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity<>(catOptional.get(), HttpStatus.OK) ; 
        }
    }

    //Get quizzes of a category
    @GetMapping("/categories/{id}/quizzes")
    @ResponseBody
    public ResponseEntity<?> getQuizByCategoryId(@PathVariable Long id) {
        Optional<Category> catOptional = categoryRepository.findById(id); 

        if (catOptional.isEmpty()){
            return new ResponseEntity<>("Category with the provided ID does not exist", HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity<>(catOptional.get().getQuizzes(),HttpStatus.OK);
        }
    }
    
    // Submit answer for a question
    @PostMapping("questions/{questionId}/answers")
    public ResponseEntity<String> submitAnswer(
        @PathVariable Long questionId, 
        @RequestBody @Validated CreateAnswerOptionDTO dto) {
        
        // Validate the question exists
        Optional<Question> questionOptional = questionRepository.findById(questionId);        
        if (questionOptional.isEmpty()) {
            return new ResponseEntity<>("Question with the provided ID not found",HttpStatus.NOT_FOUND);
        }
        Question question = questionOptional.get();

        // Validate the quiz is published
        if (!question.getQuiz().isPublished()) {
            return new ResponseEntity<>("Quiz with the provided ID is not published", HttpStatus.BAD_REQUEST);
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
    
    // Get the result of a quiz
    @GetMapping("quizzes/{quizId}/result")
    public ResponseEntity<?> getQuizResults(@PathVariable Long quizId) {
        // Validate the quiz exists and is published
        Optional<Quiz> quizOptional = quizRepository.findById(quizId);
        if (quizOptional.isEmpty()) {
            return new ResponseEntity<>("Quiz with the provided ID not found", HttpStatus.NOT_FOUND);
        }

        Quiz quiz = quizOptional.get();

        List<QuestionResultDTO> results = new ArrayList<>();

        for (Question question: quiz.getQuestions()) {
            List<StudentAnswer> studentAnswers = studentAnswerRepository.findByAnswerQuestionId(question.getId());

            long totalAnswers = studentAnswers.size();
            long correctAnswers = studentAnswers.stream().filter(studentAnswer -> studentAnswer.getAnswer().getIsCorrect()).count();
            long wrongAnswers = totalAnswers - correctAnswers;

            QuestionResultDTO resultDTO = new QuestionResultDTO(
                question.getId(),
                question.getQuestionText(),
                question.getDifficulty().toString(),
                totalAnswers,
                correctAnswers,
                wrongAnswers
            );

            results.add(resultDTO);
        }

        return new ResponseEntity<>(results, HttpStatus.OK);
    }    
}
