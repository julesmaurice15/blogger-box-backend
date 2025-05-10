package com.dauphine.blogger_box_backend.controllers;

import com.dauphine.blogger_box_backend.dto.PostDTO;
import com.dauphine.blogger_box_backend.exception.CategoryNotFoundException;
import com.dauphine.blogger_box_backend.exception.PostNotFoundException;
import com.dauphine.blogger_box_backend.model.Post;
import com.dauphine.blogger_box_backend.service.PostService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1/posts")
@Tag(name = "Post", description = "Post management APIs")
public class PostController {

    private final PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping
    @Operation(
            summary = "Get all posts",
            description = "Retrieves a list of all posts ordered by creation date (newest first)"
    )
    public ResponseEntity<List<PostDTO>> getAllPosts(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(required = false) String value) {

        List<Post> posts;

        if (date != null) {
            posts = postService.getPostsByDate(date);
        } else if (value != null && !value.isBlank()) {
            posts = postService.getPostsByTitleOrContent(value);
        } else {
            posts = postService.getAllPosts();
        }

        List<PostDTO> postDTOs = posts.stream()
                .map(post -> new PostDTO(
                        post.getId(),
                        post.getTitle(),
                        post.getContent(),
                        post.getCreatedDate().toLocalDate(),
                        post.getCategory().getId()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(postDTOs);
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Get post by ID",
            description = "Retrieves a post by its ID"
    )
    @ApiResponse(responseCode = "200", description = "Post found")
    @ApiResponse(responseCode = "404", description = "Post not found")
    public ResponseEntity<PostDTO> getPostById(@PathVariable UUID id) throws PostNotFoundException {
        Post post = postService.getPostById(id);

        PostDTO postDTO = new PostDTO(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getCreatedDate().toLocalDate(),
                post.getCategory().getId());

        return ResponseEntity.ok(postDTO);
    }

    @PostMapping
    @Operation(
            summary = "Create a new post",
            description = "Creates a new post with the provided information"
    )
    @ApiResponse(responseCode = "201", description = "Post created successfully")
    @ApiResponse(responseCode = "404", description = "Category not found")
    public ResponseEntity<PostDTO> createPost(@RequestBody PostDTO postDTO)
            throws CategoryNotFoundException {

        Post createdPost = postService.createPost(
                postDTO.getTitle(),
                postDTO.getContent(),
                postDTO.getCategoryId());

        PostDTO createdPostDTO = new PostDTO(
                createdPost.getId(),
                createdPost.getTitle(),
                createdPost.getContent(),
                createdPost.getCreatedDate().toLocalDate(),
                createdPost.getCategory().getId());

        return ResponseEntity
                .created(URI.create("/v1/posts/" + createdPost.getId()))
                .body(createdPostDTO);
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Update a post",
            description = "Updates an existing post's information"
    )
    @ApiResponse(responseCode = "200", description = "Post updated successfully")
    @ApiResponse(responseCode = "404", description = "Post not found or Category not found")
    public ResponseEntity<PostDTO> updatePost(@PathVariable UUID id, @RequestBody PostDTO postDTO)
            throws PostNotFoundException, CategoryNotFoundException {

        Post updatedPost = postService.updatePost(
                id,
                postDTO.getTitle(),
                postDTO.getContent(),
                postDTO.getCategoryId());

        PostDTO updatedPostDTO = new PostDTO(
                updatedPost.getId(),
                updatedPost.getTitle(),
                updatedPost.getContent(),
                updatedPost.getCreatedDate().toLocalDate(),
                updatedPost.getCategory().getId());

        return ResponseEntity.ok(updatedPostDTO);
    }

    @PatchMapping("/{id}")
    @Operation(
            summary = "Partially update a post",
            description = "Updates specific fields of an existing post"
    )
    @ApiResponse(responseCode = "200", description = "Post updated successfully")
    @ApiResponse(responseCode = "404", description = "Post not found or Category not found")
    public ResponseEntity<PostDTO> patchPost(@PathVariable UUID id, @RequestBody PostDTO postDTO)
            throws PostNotFoundException, CategoryNotFoundException {

        Post updatedPost = postService.updatePost(
                id,
                postDTO.getTitle(),
                postDTO.getContent(),
                postDTO.getCategoryId());

        PostDTO updatedPostDTO = new PostDTO(
                updatedPost.getId(),
                updatedPost.getTitle(),
                updatedPost.getContent(),
                updatedPost.getCreatedDate().toLocalDate(),
                updatedPost.getCategory().getId());

        return ResponseEntity.ok(updatedPostDTO);
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Delete a post",
            description = "Deletes an existing post"
    )
    @ApiResponse(responseCode = "204", description = "Post deleted successfully")
    @ApiResponse(responseCode = "404", description = "Post not found")
    public ResponseEntity<Void> deletePost(@PathVariable UUID id) throws PostNotFoundException {
        postService.deletePost(id);
        return ResponseEntity.noContent().build();
    }
}