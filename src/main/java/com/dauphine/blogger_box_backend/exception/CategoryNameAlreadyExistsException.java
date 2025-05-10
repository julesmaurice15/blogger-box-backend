package com.dauphine.blogger_box_backend.exception;

public class CategoryNameAlreadyExistsException extends RuntimeException {

    public CategoryNameAlreadyExistsException(String name) {
        super("Category with name '" + name + "' already exists");
    }
}
