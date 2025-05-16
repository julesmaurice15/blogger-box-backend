package com.dauphine.blogger_box_backend.model;


import java.util.UUID;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "category")
public class Category {
    @Id
    @Column(name = "id")
    private UUID id;

    @Column(name = "name")
    private String name;


    public Category() {
    }

    public Category(String name) {
        this.id = UUID.randomUUID();
        this.name = name;
    }


    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}