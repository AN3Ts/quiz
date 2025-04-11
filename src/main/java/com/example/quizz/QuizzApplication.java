package com.example.quizz;

import com.example.quizz.domain.Question;
import com.example.quizz.domain.Question.Difficulty;
import com.example.quizz.domain.Quiz;
import com.example.quizz.domain.QuizRepository;
import com.example.quizz.domain.QuestionRepository;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class QuizzApplication {

    public static void main(String[] args) {
        SpringApplication.run(QuizzApplication.class, args);
    }

    @Bean
    public CommandLineRunner loadData(QuizRepository quizRepository, QuestionRepository questionRepository) {
        return (args) -> {
            // Add sample quizzes
            Quiz quiz1 = new Quiz();
            quiz1.setName("Java Basics");
            quiz1.setDescription("A quiz about the basics of Java programming.");
            quiz1.setCourseCode("JAVA101");
            quiz1.setPublished(true);

            Quiz quiz2 = new Quiz();
            quiz2.setName("Spring Framework");
            quiz2.setDescription("A quiz about the Spring Framework.");
            quiz2.setCourseCode("SPRING202");
            quiz2.setPublished(false);

            Quiz quiz3 = new Quiz();
            quiz3.setName("Database Fundamentals");
            quiz3.setDescription("A quiz about database concepts and SQL.");
            quiz3.setCourseCode("DB101");
            quiz3.setPublished(true);

            // Save quizzes to the database
            quizRepository.save(quiz1);
            quizRepository.save(quiz2);
            quizRepository.save(quiz3);

			// Add sample questions to quiz1
            Question question1 = new Question();
            question1.setQuestionText("What is the size of an int in Java?");
            question1.setDifficulty(Difficulty.EASY);
            question1.setQuiz(quiz1);

            Question question2 = new Question();
            question2.setQuestionText("What is the default value of a boolean in Java?");
            question2.setDifficulty(Difficulty.NORMAL);
            question2.setQuiz(quiz1);

            Question question3 = new Question();
            question3.setQuestionText("Explain the concept of polymorphism in Java.");
            question3.setDifficulty(Difficulty.HARD);
            question3.setQuiz(quiz1);

            // Save questions to the database
            questionRepository.save(question1);
            questionRepository.save(question2);
            questionRepository.save(question3);


        };
    }
}
