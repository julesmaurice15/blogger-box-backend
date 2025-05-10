package com.dauphine.blogger_box_backend.exception;

import java.util.UUID;

public class PostNotFoundException extends RuntimeException {

    public PostNotFoundException(UUID id) {
        super("Post not found with id: " + id);
    }
}
