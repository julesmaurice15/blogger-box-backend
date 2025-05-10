package com.dauphine.blogger_box_backend.service;

import com.dauphine.blogger_box_backend.exception.CategoryNotFoundException;
import com.dauphine.blogger_box_backend.exception.PostNotFoundException;
import com.dauphine.blogger_box_backend.model.Category;
import com.dauphine.blogger_box_backend.model.Post;
import com.dauphine.blogger_box_backend.repository.PostRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Service
public class PostService {

    private final PostRepository repository;
    private final CategoryService categoryService;

    public PostService(PostRepository repository, CategoryService categoryService) {
        this.repository = repository;
        this.categoryService = categoryService;
    }

    public List<Post> getAllPosts() {
        return repository.findAll();
    }

    public Post getPostById(UUID id) throws PostNotFoundException {
        return repository.findById(id)
                .orElseThrow(() -> new PostNotFoundException(id));
    }

    public Post createPost(String title, String content, UUID categoryId)
            throws CategoryNotFoundException {
        Category category = categoryService.getById(categoryId);
        Post post = new Post(title, content, category);
        return repository.save(post);
    }

    public Post updatePost(UUID id, String title, String content, UUID categoryId)
            throws PostNotFoundException, CategoryNotFoundException {
        Post post = getPostById(id);

        if (title != null) {
            post.setTitle(title);
        }

        if (content != null) {
            post.setContent(content);
        }

        if (categoryId != null) {
            Category category = categoryService.getById(categoryId);
            post.setCategory(category);
        }

        return repository.save(post);
    }

    public boolean deletePost(UUID id) throws PostNotFoundException {
        if (!repository.existsById(id)) {
            throw new PostNotFoundException(id);
        }
        repository.deleteById(id);
        return true;
    }

    public List<Post> getPostsByDate(LocalDate date) {
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(LocalTime.MAX);

        return repository.findAll().stream()
                .filter(post -> {
                    LocalDateTime postDate = post.getCreatedDate();
                    return postDate.isAfter(startOfDay) && postDate.isBefore(endOfDay);
                })
                .toList();
    }

    public List<Post> getPostsByCategoryId(UUID categoryId) throws CategoryNotFoundException {
        Category category = categoryService.getById(categoryId);
        return repository.findByCategoryOrderByCreatedDateDesc(category);
    }

    public List<Post> getPostsByTitleOrContent(String value) {
        return repository.findByTitleOrContentContaining(value);
    }
}
