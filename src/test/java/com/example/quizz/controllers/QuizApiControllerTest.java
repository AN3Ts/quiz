package com.example.quizz.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import com.example.quizz.domain.AnswerRepository;
import com.example.quizz.domain.QuestionRepository;
import com.example.quizz.domain.Quiz;
import com.example.quizz.domain.QuizRepository;
import com.example.quizz.domain.StudentAnswerRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.is;

import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import org.springframework.http.MediaType;

@SpringBootTest
@AutoConfigureMockMvc
public class QuizApiControllerTest {
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
        studentAnswerRepository.deleteAll();
        answerRepository.deleteAll();
        questionRepository.deleteAll();
        quizRepository.deleteAll();
    }

    @Test
    void getAllQuizzesReturnsEmptyListWhenNoQuizzesExist() throws Exception {
        mockMvc.perform(get("/api/quizzes")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void getAllQuizzesReturnsListOfQuizzesWhenPublishedQuizzesExist() throws Exception {
        Quiz quiz1 = new Quiz("Quiz 1", "Description 1", "CODE1234", true, null, null, null);
        Quiz quiz2 = new Quiz("Quiz 2", "Description 2", "CODE5678", true, null, null, null);
        quizRepository.saveAll(List.of(quiz1, quiz2));

        mockMvc.perform(get("/api/quizzes")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is("Quiz 1")))
                .andExpect(jsonPath("$[1].name", is("Quiz 2")));
    }

    @Test
    void getAllQuizzesDoesNotReturnUnpublishedQuizzes() throws Exception {
        Quiz publishedQuiz = new Quiz("Published Quiz", "Desc", "CODE9999", true, null, null, null);
        Quiz unpublishedQuiz = new Quiz("Unpublished Quiz", "Hidden", "CODE0000", false, null, null, null);
        quizRepository.saveAll(List.of(publishedQuiz, unpublishedQuiz));

        mockMvc.perform(get("/api/quizzes")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is("Published Quiz")));
    }
}
