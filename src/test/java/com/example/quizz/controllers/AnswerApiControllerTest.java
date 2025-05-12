package com.example.quizz.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.example.quizz.domain.Answer;
import com.example.quizz.domain.AnswerRepository;
import com.example.quizz.domain.Question;
import com.example.quizz.domain.QuestionRepository;
import com.example.quizz.domain.Quiz;
import com.example.quizz.domain.QuizRepository;
import com.example.quizz.domain.StudentAnswer;
import com.example.quizz.domain.StudentAnswerRepository;
import com.example.quizz.dto.CreateAnswerOptionDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest
@AutoConfigureMockMvc
public class AnswerApiControllerTest {
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
    public void createAnswerSavesAnswerForPublishedQuiz() throws Exception {
        Quiz quiz = new Quiz();
        quiz.setName("Test Quiz");
        quiz.setDescription("Test Description");
        quiz.setPublished(true);
        Quiz savedQuiz = quizRepository.save(quiz);

        Question question = new Question();
        question.setQuiz(savedQuiz);
        question.setQuestionText("1 + 1 = ?");
        question.setDifficulty(Question.Difficulty.EASY);
        Question savedQuestion = questionRepository.save(question);

        Answer answer = new Answer();
        answer.setQuestion(savedQuestion);
        answer.setAnswerText("Option 1");
        answer.setIsCorrect(true);
        answerRepository.save(answer);

        CreateAnswerOptionDTO answerOptionDTO = new CreateAnswerOptionDTO();
        answerOptionDTO.setAnswerOptionId(answer.getId());
        String requestBody = mapper.writeValueAsString(answerOptionDTO);


        mockMvc.perform(post("/api/questions/"+ savedQuestion.getId() + "/answers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().is2xxSuccessful());

        List<Answer> savedAnswers = answerRepository.findAll();
        assertEquals(1, savedAnswers.size());
        assertEquals("Option 1", savedAnswers.get(0).getAnswerText());
    }

    @Test
    public void createAnswerDoesNotSaveAnswerWithoutAnswerOption() throws Exception {
        Quiz quiz = new Quiz();
        quiz.setName("Test Quiz");
        quiz.setDescription("Test Description");
        quiz.setPublished(true);
        Quiz savedQuiz = quizRepository.save(quiz);

        Question question = new Question();
        question.setQuiz(savedQuiz);
        question.setQuestionText("1 + 1 = ?");
        question.setDifficulty(Question.Difficulty.EASY);
        Question savedQuestion = questionRepository.save(question);

        Answer answer = new Answer();
        answer.setQuestion(savedQuestion);
        answer.setAnswerText("Option 1");
        answer.setIsCorrect(true);
        answerRepository.save(answer);

        CreateAnswerOptionDTO answerOptionDTO = new CreateAnswerOptionDTO();
        answerOptionDTO.setAnswerOptionId(null);
        String requestBody = mapper.writeValueAsString(answerOptionDTO);

        mockMvc.perform(post("/api/questions/"+ savedQuestion.getId() + "/answers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isBadRequest());

        List<StudentAnswer> studentAnswer = studentAnswerRepository.findAll();
        assertEquals(0, studentAnswer.size());
    }

    @Test
    public void createAnswerDoesNotSaveAnswerForNonExistingAnswerOption() throws Exception {
        Quiz quiz = new Quiz();
        quiz.setName("Test Quiz");
        quiz.setDescription("Test Description");
        quiz.setPublished(true);
        Quiz savedQuiz = quizRepository.save(quiz);

        Question question = new Question();
        question.setQuiz(savedQuiz);
        question.setQuestionText("1 + 1 = ?");
        question.setDifficulty(Question.Difficulty.EASY);
        Question savedQuestion = questionRepository.save(question);

        Answer answer = new Answer();
        answer.setQuestion(savedQuestion);
        answer.setAnswerText("Option 1");
        answer.setIsCorrect(true);
        answerRepository.save(answer);

        CreateAnswerOptionDTO answerOptionDTO = new CreateAnswerOptionDTO();
        answerOptionDTO.setAnswerOptionId(999L); // Non-existing ID
        String requestBody = mapper.writeValueAsString(answerOptionDTO);

        mockMvc.perform(post("/api/questions/"+ savedQuestion.getId() + "/answers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().is4xxClientError());

        List<StudentAnswer> studentAnswer = studentAnswerRepository.findAll();
        assertEquals(0, studentAnswer.size());
    }

    @Test
    public void createAnswerDoesNotSaveAnswerForNonPublishedQuiz() throws Exception {
        Quiz quiz = new Quiz();
        quiz.setName("Test Quiz");
        quiz.setDescription("Test Description");
        quiz.setPublished(false);
        Quiz savedQuiz = quizRepository.save(quiz);

        Question question = new Question();
        question.setQuiz(savedQuiz);
        question.setQuestionText("1 + 1 = ?");
        question.setDifficulty(Question.Difficulty.EASY);
        Question savedQuestion = questionRepository.save(question);

        Answer answer = new Answer();
        answer.setQuestion(savedQuestion);
        answer.setAnswerText("Option 1");
        answer.setIsCorrect(true);
        answerRepository.save(answer);

        CreateAnswerOptionDTO answerOptionDTO = new CreateAnswerOptionDTO();
        answerOptionDTO.setAnswerOptionId(answer.getId());
        String requestBody = mapper.writeValueAsString(answerOptionDTO);

        mockMvc.perform(post("/api/questions/"+ savedQuestion.getId() + "/answers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().is4xxClientError());

        List<StudentAnswer> studentAnswer = studentAnswerRepository.findAll();
        assertEquals(0, studentAnswer.size());
    }
}


