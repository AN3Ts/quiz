package com.example.quizz.dto;

import jakarta.validation.constraints.NotNull;

public class CreateAnswerOptionDTO {
    @NotNull(message = "Answer option text cannot be null")
    private Long answerOptionId;

    public Long getAnswerOptionId() {
        return answerOptionId;
    }

    public void setAnswerOptionId(Long answerOptionId) {
        this.answerOptionId = answerOptionId;
    }
}
