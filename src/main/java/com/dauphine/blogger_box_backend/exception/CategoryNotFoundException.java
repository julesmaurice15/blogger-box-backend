package com.dauphine.blogger_box_backend.exception;

import java.util.UUID;

public class CategoryNotFoundException extends RuntimeException {

    public CategoryNotFoundException(UUID id) {
        super("Category not found with id: " + id);
    }
}
