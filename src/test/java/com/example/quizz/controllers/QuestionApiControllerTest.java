package com.example.quizz.controllers;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import com.example.quizz.domain.AnswerRepository;
import com.example.quizz.domain.Question;
import com.example.quizz.domain.QuestionRepository;
import com.example.quizz.domain.Quiz;
import com.example.quizz.domain.QuizRepository;
import com.example.quizz.domain.StudentAnswerRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import org.springframework.http.MediaType;


@SpringBootTest
@AutoConfigureMockMvc
public class QuestionApiControllerTest {
    @Autowired
    AnswerRepository answerRepository;

    @Autowired
    QuizRepository quizRepository;

    @Autowired
    QuestionRepository questionRepository;

    @Autowired
    StudentAnswerRepository studentAnswerRepository;

    @Autowired
    private MockMvc mockMvc;

    ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    void setUp() throws Exception {
        answerRepository.deleteAll();
        questionRepository.deleteAll();        
        quizRepository.deleteAll();       
    }

    @Test
    void getQuestionsByQuizIdReturnsEmptyListWhenQuizDoesNotHaveQuestions() throws Exception{
        // save a quiz without questions to the database and send a request. Then, the response should have an empty list

        Quiz quiz = new Quiz();
        quiz.setName("Test Quiz");
        quiz.setDescription("Test Description");
        quiz.setPublished(true);
        Quiz savedQuiz = quizRepository.save(quiz);
        Long quizId = savedQuiz.getId(); 

        mockMvc.perform(get("/api/quizzes/" + quizId + "/questions")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }


    @Test
    public void getQuestionsByQuizIdReturnsListOfQuestionsWhenQuizHasQuestions() throws Exception{
        //save a quiz with a few questions and answer options to the database and send a request. Then, the response should have a list of the quiz’s questions. Remember to also check, that the response contains the answer options of each question

        Quiz quiz = new Quiz();
        quiz.setName("Test Quiz");
        quiz.setDescription("Test Description");
        quiz.setPublished(true);
        Quiz savedQuiz = quizRepository.save(quiz);
        Long quizId = savedQuiz.getId(); 

        Question question1 = new Question();
        question1.setQuiz(savedQuiz);
        question1.setQuestionText("1 + 1 = ?");
        question1.setDifficulty(Question.Difficulty.EASY);
        questionRepository.save(question1);

        Question question2 = new Question();
        question2.setQuiz(savedQuiz);
        question2.setQuestionText("1 + 2 = ?");
        question2.setDifficulty(Question.Difficulty.EASY);
        questionRepository.save(question2);

        mockMvc.perform(get("/api/quizzes/" + quizId + "/questions")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$",hasSize(2))); 
    }

    @Test
    public void getQuestionsByQuizIdReturnsErrorWhenQuizDoesNotExist() throws Exception{
        //send a request without saving a quiz to the database. Then, the response should have an appropriate HTTP status
        Long nonExistingId = 1212412L;

        mockMvc.perform(get("/api/quizzes/" + nonExistingId + "/questions")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound()); 
    }


   
 

}
