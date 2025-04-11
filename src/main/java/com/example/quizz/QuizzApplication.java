package com.example.quizz;

import com.example.quizz.domain.Quiz;
import com.example.quizz.domain.QuizRepository;

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
    public CommandLineRunner loadData(QuizRepository quizRepository) {
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
        };
    }
}
