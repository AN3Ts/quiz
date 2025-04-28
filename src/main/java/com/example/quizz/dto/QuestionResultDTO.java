package com.example.quizz.dto;

public class QuestionResultDTO {
    private Long questionId;
    private String questionText;
    private String questionDifficulty;
    private long totalAnswers;
    private long correctAnswers;
    private long wrongAnswers;

    public QuestionResultDTO() {
    }

    public QuestionResultDTO(Long questionId, String questionText, String questionDifficulty, long totalAnswers, long correctAnswers, long wrongAnswers) {
        this.questionId = questionId;
        this.questionText = questionText;
        this.questionDifficulty = questionDifficulty;
        this.totalAnswers = totalAnswers;
        this.correctAnswers = correctAnswers;
        this.wrongAnswers = wrongAnswers;
    }

    public Long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public String getQuestionDifficulty() {
        return questionDifficulty;
    }

    public void setQuestionDifficulty(String questionDifficulty) {
        this.questionDifficulty = questionDifficulty;
    }

    public long getTotalAnswers() {
        return totalAnswers;
    }

    public void setTotalAnswers(long totalAnswers) {
        this.totalAnswers = totalAnswers;
    }

    public long getCorrectAnswers() {
        return correctAnswers;
    }

    public void setCorrectAnswers(long correctAnswers) {
        this.correctAnswers = correctAnswers;
    }

    public long getWrongAnswers() {
        return wrongAnswers;
    }

    public void setWrongAnswers(long wrongAnswers) {
        this.wrongAnswers = wrongAnswers;
    }
}
