package com.dauphine.blogger_box_backend.service;



import com.dauphine.blogger_box_backend.model.Post;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PostService {

    private List<Post> posts = new ArrayList<>();
    private Long nextId = 1L;

    // Mock data initializer
    public PostService() {
        posts.add(new Post(nextId++, "Spring Boot Intro", "Introduction to Spring Boot", LocalDate.now(), 1L));
        posts.add(new Post(nextId++, "Paris Travel Guide", "Best places to visit in Paris", LocalDate.now().minusDays(1), 2L));
        posts.add(new Post(nextId++, "Italian Cuisine", "Exploring Italian dishes", LocalDate.now().minusDays(2), 3L));
    }

    public List<Post> getAllPosts() {
        return posts.stream()
                .sorted(Comparator.comparing(Post::getCreatedDate).reversed())
                .collect(Collectors.toList());
    }

    public Optional<Post> getPostById(Long id) {
        return posts.stream()
                .filter(post -> post.getId().equals(id))
                .findFirst();
    }

    public Post createPost(Post post) {
        post.setId(nextId++);
        if (post.getCreatedDate() == null) {
            post.setCreatedDate(LocalDate.now());
        }
        posts.add(post);
        return post;
    }

    public Optional<Post> updatePost(Long id, Post updatedPost) {
        Optional<Post> postOpt = getPostById(id);

        if (postOpt.isPresent()) {
            Post post = postOpt.get();
            post.setTitle(updatedPost.getTitle());
            post.setContent(updatedPost.getContent());
            post.setCategoryId(updatedPost.getCategoryId());
            return Optional.of(post);
        }

        return Optional.empty();
    }

    public boolean deletePost(Long id) {
        Optional<Post> postOpt = getPostById(id);

        if (postOpt.isPresent()) {
            posts.remove(postOpt.get());
            return true;
        }

        return false;
    }

    public List<Post> getPostsByDate(LocalDate date) {
        return posts.stream()
                .filter(post -> post.getCreatedDate().equals(date))
                .collect(Collectors.toList());
    }

    public List<Post> getPostsByCategoryId(Long categoryId) {
        return posts.stream()
                .filter(post -> post.getCategoryId().equals(categoryId))
                .collect(Collectors.toList());
    }
}
