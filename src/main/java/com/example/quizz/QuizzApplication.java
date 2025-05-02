package com.example.quizz;

import com.example.quizz.domain.Answer;
import com.example.quizz.domain.AnswerRepository;
import com.example.quizz.domain.CategoryRepository;
import com.example.quizz.domain.Question;
import com.example.quizz.domain.Question.Difficulty;
import com.example.quizz.domain.Quiz;
import com.example.quizz.domain.QuizRepository;
import com.example.quizz.domain.QuestionRepository;
import com.example.quizz.domain.Category;

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
    public CommandLineRunner loadData(QuizRepository quizRepository, QuestionRepository questionRepository,
            AnswerRepository answerRepository, CategoryRepository categoryRepository) {
        return (args) -> {
            //quizRepository.deleteAll();
            //categoryRepository.deleteAll();
            if (categoryRepository.count() == 0) {

                Category cat1 = new Category();
                cat1.setName("Agile");
                cat1.setDescription("Quizzes related to the Agile principles and project managemnet framework");
                categoryRepository.save(cat1);

                Category cat2 = new Category();
                cat2.setName("Software Engineering");
                cat2.setDescription("Quizzes related to software development methodologies and practices.");
                categoryRepository.save(cat2);

                if (quizRepository.count() == 0) {
                    Quiz quiz1 = new Quiz();
                    quiz1.setName("Java Basics");
                    quiz1.setDescription("A quiz about the basics of Java programming.");
                    quiz1.setCourseCode("JAVA101");
                    quiz1.setPublished(true);
                    quiz1.setCategory(cat2);

                    Quiz quiz2 = new Quiz();
                    quiz2.setName("Spring Framework");
                    quiz2.setDescription("A quiz about the Spring Framework.");
                    quiz2.setCourseCode("SPRING202");
                    quiz2.setPublished(false);
                    quiz2.setCategory(cat2);

                    Quiz quiz3 = new Quiz();
                    quiz3.setName("Database Fundamentals");
                    quiz3.setDescription("A quiz about database concepts and SQL.");
                    quiz3.setCourseCode("DB101");
                    quiz3.setPublished(true);
                    quiz3.setCategory(cat1);

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

                    // Add answers for question 1
                    Answer answer1 = new Answer();
                    answer1.setAnswerText("32 bits");
                    answer1.setQuestion(question1);
                    answer1.setIsCorrect(true);

                    Answer answer2 = new Answer();
                    answer2.setAnswerText("4 byte");
                    answer2.setQuestion(question1);
                    answer2.setIsCorrect(true);

                    Answer answer3 = new Answer();
                    answer3.setAnswerText("10 byte");
                    answer3.setQuestion(question1);
                    answer3.setIsCorrect(false);

                    // Add answers for question 2
                    Answer answer4 = new Answer();
                    answer4.setAnswerText("false");
                    answer4.setQuestion(question2);
                    answer4.setIsCorrect(true);

                    Answer answer5 = new Answer();
                    answer5.setAnswerText("true");
                    answer5.setQuestion(question2);
                    answer5.setIsCorrect(false);

                    Answer answer6 = new Answer();
                    answer6.setAnswerText("null");
                    answer6.setQuestion(question2);
                    answer6.setIsCorrect(false);

                    // Add answers for question 3
                    Answer answer7 = new Answer();
                    answer7.setAnswerText("The ability of an object to take many forms");
                    answer7.setQuestion(question3);
                    answer7.setIsCorrect(true);

                    Answer answer8 = new Answer();
                    answer8.setAnswerText("The process of hiding internal details");
                    answer8.setQuestion(question3);
                    answer8.setIsCorrect(false); // This refers more to encapsulation

                    Answer answer9 = new Answer();
                    answer9.setAnswerText("The technique of defining multiple constructors");
                    answer9.setQuestion(question3);
                    answer9.setIsCorrect(false);

                    // Save questions to the database
                    questionRepository.save(question1);
                    questionRepository.save(question2);
                    questionRepository.save(question3);

                    // Save answers for question 1
                    answerRepository.save(answer1);
                    answerRepository.save(answer2);
                    answerRepository.save(answer3);

                    // Save answers for question 2
                    answerRepository.save(answer4);
                    answerRepository.save(answer5);
                    answerRepository.save(answer6);

                    // Save answers for question 3
                    answerRepository.save(answer7);
                    answerRepository.save(answer8);
                    answerRepository.save(answer9);

                    System.out.println("question1's answers: " + question1.getAnswers());
                    System.out.println("question3's answers: " + question3.getAnswers());
                    System.out.println("quiz1's questions: " + quiz1.getQuestions());
                }
            }
        };
    }
}
