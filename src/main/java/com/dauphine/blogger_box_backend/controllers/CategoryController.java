package com.dauphine.blogger_box_backend.controllers;

import com.dauphine.blogger_box_backend.dto.CategoryDTO;
import com.dauphine.blogger_box_backend.dto.PostDTO;
import com.dauphine.blogger_box_backend.model.Category;
import com.dauphine.blogger_box_backend.model.Post;
import com.dauphine.blogger_box_backend.service.CategoryService;
import com.dauphine.blogger_box_backend.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1/categories")
@Tag(name = "Category", description = "Category management APIs")
public class CategoryController {

    private final CategoryService categoryService;
    private final PostService postService;

    @Autowired
    public CategoryController(CategoryService categoryService, PostService postService) {
        this.categoryService = categoryService;
        this.postService = postService;
    }

    @GetMapping
    @Operation(summary = "Get all categories", description = "Retrieves a list of all available categories")
    public ResponseEntity<List<CategoryDTO>> getAllCategories() {
        List<Category> categories = categoryService.getAllCategories();
        List<CategoryDTO> categoryDTOs = categories.stream()
                .map(category -> new CategoryDTO(category.getId(), category.getName()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(categoryDTOs);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get category by ID", description = "Retrieves a category by its ID")
    @ApiResponse(responseCode = "200", description = "Category found")
    @ApiResponse(responseCode = "404", description = "Category not found")
    public ResponseEntity<CategoryDTO> getCategoryById(@PathVariable Long id) {
        Optional<Category> categoryOpt = categoryService.getCategoryById(id);
        return categoryOpt.map(category -> ResponseEntity.ok(new CategoryDTO(category.getId(), category.getName())))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Create a new category", description = "Creates a new category with the provided information")
    @ApiResponse(responseCode = "201", description = "Category created successfully")
    public ResponseEntity<CategoryDTO> createCategory(@RequestBody CategoryDTO categoryDTO) {
        Category category = new Category();
        category.setName(categoryDTO.getName());

        Category createdCategory = categoryService.createCategory(category);
        CategoryDTO createdCategoryDTO = new CategoryDTO(createdCategory.getId(), createdCategory.getName());

        return ResponseEntity.status(HttpStatus.CREATED).body(createdCategoryDTO);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a category", description = "Updates an existing category's information")
    @ApiResponse(responseCode = "200", description = "Category updated successfully")
    @ApiResponse(responseCode = "404", description = "Category not found")
    public ResponseEntity<CategoryDTO> updateCategory(@PathVariable Long id, @RequestBody CategoryDTO categoryDTO) {
        Category category = new Category();
        category.setName(categoryDTO.getName());

        Optional<Category> updatedCategoryOpt = categoryService.updateCategory(id, category);

        return updatedCategoryOpt.map(updatedCategory ->
                        ResponseEntity.ok(new CategoryDTO(updatedCategory.getId(), updatedCategory.getName())))
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a category", description = "Deletes an existing category")
    @ApiResponse(responseCode = "204", description = "Category deleted successfully")
    @ApiResponse(responseCode = "404", description = "Category not found")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        boolean deleted = categoryService.deleteCategory(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}/posts")
    @Operation(summary = "Get posts by category", description = "Retrieves all posts that belong to a specific category")
    @ApiResponse(responseCode = "200", description = "Posts retrieved successfully")
    @ApiResponse(responseCode = "404", description = "Category not found")
    public ResponseEntity<List<PostDTO>> getPostsByCategory(@PathVariable Long id) {
        Optional<Category> categoryOpt = categoryService.getCategoryById(id);

        if (categoryOpt.isPresent()) {
            List<Post> posts = postService.getPostsByCategoryId(id);
            List<PostDTO> postDTOs = posts.stream()
                    .map(post -> new PostDTO(post.getId(), post.getTitle(), post.getContent(),
                            post.getCreatedDate(), post.getCategoryId()))
                    .collect(Collectors.toList());
            return ResponseEntity.ok(postDTOs);
        }

        return ResponseEntity.notFound().build();
    }
}