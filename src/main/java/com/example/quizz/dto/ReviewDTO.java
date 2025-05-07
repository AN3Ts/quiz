package com.example.quizz.dto;

import java.time.LocalDateTime;

public class ReviewDTO {

    private Long id;
    private String content;
    private String nickname;
    private int rating;
    private LocalDateTime createdDate;

    // Constructor
    public ReviewDTO() {
    }

    public ReviewDTO(Long id, String content, String nickname, int rating, LocalDateTime createdDate) {
        this.id = id;
        this.content = content;
        this.nickname = nickname;
        this.rating = rating;
        this.createdDate = createdDate;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }
}