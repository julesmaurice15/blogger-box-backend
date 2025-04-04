package com.dauphine.blogger_box_backend.controllers;

import com.dauphine.blogger_box_backend.dto.PostDTO;
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

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
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
    @Operation(summary = "Get all posts", description = "Retrieves a list of all posts ordered by creation date (newest first)")
    public ResponseEntity<List<PostDTO>> getAllPosts(@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<Post> posts;

        if (date != null) {
            posts = postService.getPostsByDate(date);
        } else {
            posts = postService.getAllPosts();
        }

        List<PostDTO> postDTOs = posts.stream()
                .map(post -> new PostDTO(post.getId(), post.getTitle(), post.getContent(),
                        post.getCreatedDate(), post.getCategoryId()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(postDTOs);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get post by ID", description = "Retrieves a post by its ID")
    @ApiResponse(responseCode = "200", description = "Post found")
    @ApiResponse(responseCode = "404", description = "Post not found")
    public ResponseEntity<PostDTO> getPostById(@PathVariable Long id) {
        Optional<Post> postOpt = postService.getPostById(id);

        return postOpt.map(post -> ResponseEntity.ok(
                        new PostDTO(post.getId(), post.getTitle(), post.getContent(),
                                post.getCreatedDate(), post.getCategoryId())))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Create a new post", description = "Creates a new post with the provided information")
    @ApiResponse(responseCode = "201", description = "Post created successfully")
    public ResponseEntity<PostDTO> createPost(@RequestBody PostDTO postDTO) {
        Post post = new Post();
        post.setTitle(postDTO.getTitle());
        post.setContent(postDTO.getContent());
        post.setCreatedDate(postDTO.getCreatedDate() != null ? postDTO.getCreatedDate() : LocalDate.now());
        post.setCategoryId(postDTO.getCategoryId());

        Post createdPost = postService.createPost(post);
        PostDTO createdPostDTO = new PostDTO(createdPost.getId(), createdPost.getTitle(),
                createdPost.getContent(), createdPost.getCreatedDate(), createdPost.getCategoryId());

        return ResponseEntity.status(HttpStatus.CREATED).body(createdPostDTO);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a post", description = "Updates an existing post's information")
    @ApiResponse(responseCode = "200", description = "Post updated successfully")
    @ApiResponse(responseCode = "404", description = "Post not found")
    public ResponseEntity<PostDTO> updatePost(@PathVariable Long id, @RequestBody PostDTO postDTO) {
        Post post = new Post();
        post.setTitle(postDTO.getTitle());
        post.setContent(postDTO.getContent());
        post.setCategoryId(postDTO.getCategoryId());

        Optional<Post> updatedPostOpt = postService.updatePost(id, post);

        return updatedPostOpt.map(updatedPost -> ResponseEntity.ok(
                        new PostDTO(updatedPost.getId(), updatedPost.getTitle(), updatedPost.getContent(),
                                updatedPost.getCreatedDate(), updatedPost.getCategoryId())))
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Partially update a post", description = "Updates specific fields of an existing post")
    @ApiResponse(responseCode = "200", description = "Post updated successfully")
    @ApiResponse(responseCode = "404", description = "Post not found")
    public ResponseEntity<PostDTO> patchPost(@PathVariable Long id, @RequestBody PostDTO postDTO) {
        Optional<Post> existingPostOpt = postService.getPostById(id);

        if (existingPostOpt.isPresent()) {
            Post existingPost = existingPostOpt.get();

            // Only update fields that are provided
            if (postDTO.getTitle() != null) {
                existingPost.setTitle(postDTO.getTitle());
            }
            if (postDTO.getContent() != null) {
                existingPost.setContent(postDTO.getContent());
            }
            if (postDTO.getCategoryId() != null) {
                existingPost.setCategoryId(postDTO.getCategoryId());
            }

            Optional<Post> updatedPostOpt = postService.updatePost(id, existingPost);

            return updatedPostOpt.map(updatedPost -> ResponseEntity.ok(
                            new PostDTO(updatedPost.getId(), updatedPost.getTitle(), updatedPost.getContent(),
                                    updatedPost.getCreatedDate(), updatedPost.getCategoryId())))
                    .orElse(ResponseEntity.notFound().build());
        }

        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a post", description = "Deletes an existing post")
    @ApiResponse(responseCode = "204", description = "Post deleted successfully")
    @ApiResponse(responseCode = "404", description = "Post not found")
    public ResponseEntity<Void> deletePost(@PathVariable Long id) {
        boolean deleted = postService.deletePost(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
