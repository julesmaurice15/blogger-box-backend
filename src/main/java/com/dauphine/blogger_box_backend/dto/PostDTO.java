package com.dauphine.blogger_box_backend.dto;

import java.time.LocalDate;
import java.util.UUID;

public class PostDTO {
    private UUID id;
    private String title;
    private String content;
    private LocalDate createdDate;
    private UUID categoryId;

    public PostDTO() {
    }

    public PostDTO(UUID id, String title, String content, LocalDate createdDate, UUID categoryId) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.createdDate = createdDate;
        this.categoryId = categoryId;
    }

    // Getters et setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDate getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
    }

    public UUID getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(UUID categoryId) {
        this.categoryId = categoryId;
    }
}