package com.example.quizz.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Question text cannot be blank")
    @Size(max = 500, message = "Question text must not exceed 500 characters")
    private String questionText;

    @Enumerated(EnumType.STRING)
    private Difficulty difficulty;

    @ManyToOne
    @JoinColumn(name = "quiz_id", nullable = false) // Foreign key column in the Question table
    @JsonBackReference
    private Quiz quiz;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    public Quiz getQuiz() {
        return quiz;
    }

    public void setQuiz(Quiz quiz) {
        this.quiz = quiz;
    }

    public enum Difficulty {
        EASY,
        NORMAL,
        HARD
    }
}