package com.example.quizz.controllers;

import org.checkerframework.checker.units.qual.s;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import com.example.quizz.domain.AnswerRepository;
import com.example.quizz.domain.QuestionRepository;
import com.example.quizz.domain.QuizRepository;
import com.example.quizz.domain.StudentAnswerRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

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
        quizRepository.deleteAll();
        questionRepository.deleteAll();
        answerRepository.deleteAll();
    }

    @Test
    public void getQuestionsByQuizIdReturnsEmptyListWhenQuizDoesNotHaveQuestions() throws Exception{
        // save a quiz without questions to the database and send a request. Then, the response should have an empty list
    }

    @Test
    public void getQuestionsByQuizIdReturnsListOfQuestionsWhenQuizHasQuestions() throws Exception{
        //save a quiz with a few questions and answer options to the database and send a request. Then, the response should have a list of the quiz’s questions. Remember to also check, that the response contains the answer options of each question
    }

    @Test
    public void getQuestionsByQuizIdReturnsErrorWhenQuizDoesNotExist() throws Exception{
        //send a request without saving a quiz to the database. Then, the response should have an appropriate HTTP status

    }


   
 

}
