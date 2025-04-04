package com.dauphine.blogger_box_backend.service;

import com.dauphine.blogger_box_backend.model.Category;
import com.dauphine.blogger_box_backend.model.Post;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    private List<Category> categories = new ArrayList<>();
    private Long nextId = 1L;

    // Mock data initializer
    public CategoryService() {
        categories.add(new Category(nextId++, "Technology"));
        categories.add(new Category(nextId++, "Travel"));
        categories.add(new Category(nextId++, "Food"));
    }

    public List<Category> getAllCategories() {
        return categories;
    }

    public Optional<Category> getCategoryById(Long id) {
        return categories.stream()
                .filter(category -> category.getId().equals(id))
                .findFirst();
    }

    public Category createCategory(Category category) {
        category.setId(nextId++);
        categories.add(category);
        return category;
    }

    public Optional<Category> updateCategory(Long id, Category updatedCategory) {
        Optional<Category> categoryOpt = getCategoryById(id);

        if (categoryOpt.isPresent()) {
            Category category = categoryOpt.get();
            category.setName(updatedCategory.getName());
            return Optional.of(category);
        }

        return Optional.empty();
    }

    public boolean deleteCategory(Long id) {
        Optional<Category> categoryOpt = getCategoryById(id);

        if (categoryOpt.isPresent()) {
            categories.remove(categoryOpt.get());
            return true;
        }

        return false;
    }

    public List<Post> getPostsByCategory(Long categoryId, List<Post> allPosts) {
        return allPosts.stream()
                .filter(post -> post.getCategoryId().equals(categoryId))
                .collect(Collectors.toList());
    }
}
