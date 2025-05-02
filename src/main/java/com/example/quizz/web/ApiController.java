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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import com.example.quizz.dto.CreateAnswerOptionDTO;
import com.example.quizz.dto.QuestionResultDTO;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
@Tag(name = "API", description = "Operations for managing quizzes, categories, questions, and answers")
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

    @Operation(summary = "Get all published quizzes", description = "Retrieve a list of all published quizzes")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Quizzes retrieved successfully")
    })
    @Tag(name = "Quizzes", description = "Endpoints for managing quizzes")
    @GetMapping("/quizzes")
    @ResponseBody
    public List<Quiz> getQuiz() {
        List<Quiz> allQuizzes = quizRepository.findAll();
        return allQuizzes.stream().filter(Quiz::isPublished).toList();
    }

    @Operation(summary = "Get all categories", description = "Retrieve a list of all categories")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Categories retrieved successfully")
    })
    @Tag(name = "Categories", description = "Endpoints for managing categories")
    @GetMapping("/categories")
    @ResponseBody
    public List<Category> getCategory() {
        return categoryRepository.findAll();
    }

    @Operation(summary = "Get a quiz by ID", description = "Retrieve a quiz by its unique ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Quiz retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Quiz with the provided ID does not exist")
    })
    @Tag(name = "Quizzes", description = "Endpoints for managing quizzes")
    @GetMapping("/quizzes/{id}")
    @ResponseBody
    public ResponseEntity<?> getQuizById(@PathVariable Long id) {
        Optional<Quiz> quizOptional = quizRepository.findById(id);
        if (quizOptional.isEmpty()) {
            return new ResponseEntity<>("Quiz with the provided ID does not exist!", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(quizOptional.get(), HttpStatus.OK);
    }

    @Operation(summary = "Get questions for a quiz", description = "Retrieve all questions for a specific quiz")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Questions retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Quiz with the provided ID does not exist")
    })
    @Tag(name = "Questions", description = "Endpoints for managing questions")
    @GetMapping("/quizzes/{id}/questions")
    @ResponseBody
    public ResponseEntity<?> getQuizQuestions(@PathVariable Long id) {
        Optional<Quiz> quizOptional = quizRepository.findById(id);
        if (quizOptional.isEmpty()) {
            return new ResponseEntity<>("Quiz with the provided ID does not exist!", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(quizOptional.get().getQuestions(), HttpStatus.OK);
    }

    @Operation(summary = "Get a category by ID", description = "Retrieve a category by its unique ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Category retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Category with the provided ID does not exist")
    })
    @Tag(name = "Categories", description = "Endpoints for managing categories")
    @GetMapping("/categories/{id}")
    @ResponseBody
    public ResponseEntity<?> getCategoryById(@PathVariable Long id) {
        Optional<Category> catOptional = categoryRepository.findById(id);
        if (catOptional.isEmpty()) {
            return new ResponseEntity<>("Category with the provided ID does not exist", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(catOptional.get(), HttpStatus.OK);
    }

    @Operation(summary = "Get quizzes for a category", description = "Retrieve all quizzes for a specific category")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Quizzes retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Category with the provided ID does not exist")
    })
    @Tag(name = "Quizzes", description = "Endpoints for managing quizzes")
    @GetMapping("/categories/{id}/quizzes")
    @ResponseBody
    public ResponseEntity<?> getQuizByCategoryId(@PathVariable Long id) {
        Optional<Category> catOptional = categoryRepository.findById(id);
        if (catOptional.isEmpty()) {
            return new ResponseEntity<>("Category with the provided ID does not exist", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(catOptional.get().getQuizzes(), HttpStatus.OK);
    }

    @Operation(summary = "Submit an answer for a question", description = "Submit an answer for a specific question")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Answer submitted successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "404", description = "Question or answer option not found")
    })
    @Tag(name = "Answers", description = "Endpoints for managing answers")
    @PostMapping("questions/{questionId}/answers")
    public ResponseEntity<String> submitAnswer(
            @PathVariable Long questionId,
            @RequestBody @Validated CreateAnswerOptionDTO dto) {
        Optional<Question> questionOptional = questionRepository.findById(questionId);
        if (questionOptional.isEmpty()) {
            return new ResponseEntity<>("Question with the provided ID not found", HttpStatus.NOT_FOUND);
        }
        Question question = questionOptional.get();

        if (!question.getQuiz().isPublished()) {
            return new ResponseEntity<>("Quiz with the provided ID is not published", HttpStatus.BAD_REQUEST);
        }

        if (dto.getAnswerOptionId() == null) {
            return new ResponseEntity<>("Answer option ID must be provided", HttpStatus.BAD_REQUEST);
        }

        Optional<Answer> answerOptionOptional = answerRepository.findById(dto.getAnswerOptionId());
        if (answerOptionOptional.isEmpty()) {
            return new ResponseEntity<>("Answer option not found", HttpStatus.NOT_FOUND);
        }

        Answer selectedAnswer = answerOptionOptional.get();
        if (!selectedAnswer.getQuestion().getId().equals(questionId)) {
            return new ResponseEntity<>("Answer option does not belong to the question", HttpStatus.BAD_REQUEST);
        }

        StudentAnswer studentAnswer = new StudentAnswer();
        studentAnswer.setAnswer(selectedAnswer);
        studentAnswerRepository.save(studentAnswer);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Operation(summary = "Get quiz results", description = "Retrieve the results of a specific quiz")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Quiz results retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Quiz with the provided ID not found")
    })
    @Tag(name = "Results", description = "Endpoints for managing quiz results")
    @GetMapping("quizzes/{quizId}/result")
    public ResponseEntity<?> getQuizResults(@PathVariable Long quizId) {
        Optional<Quiz> quizOptional = quizRepository.findById(quizId);
        if (quizOptional.isEmpty()) {
            return new ResponseEntity<>("Quiz with the provided ID not found", HttpStatus.NOT_FOUND);
        }

        Quiz quiz = quizOptional.get();
        List<QuestionResultDTO> results = new ArrayList<>();

        for (Question question : quiz.getQuestions()) {
            List<StudentAnswer> studentAnswers = studentAnswerRepository.findByAnswerQuestionId(question.getId());

            long totalAnswers = studentAnswers.size();
            long correctAnswers = studentAnswers.stream()
                    .filter(studentAnswer -> studentAnswer.getAnswer().getIsCorrect()).count();
            long wrongAnswers = totalAnswers - correctAnswers;

            QuestionResultDTO resultDTO = new QuestionResultDTO(
                    question.getId(),
                    question.getQuestionText(),
                    question.getDifficulty().toString(),
                    totalAnswers,
                    correctAnswers,
                    wrongAnswers);

            results.add(resultDTO);
        }

        return new ResponseEntity<>(results, HttpStatus.OK);
    }
}