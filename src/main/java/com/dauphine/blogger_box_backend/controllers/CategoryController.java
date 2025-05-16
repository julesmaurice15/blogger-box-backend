package com.dauphine.blogger_box_backend.controllers;

import com.dauphine.blogger_box_backend.dto.CategoryDTO;
import com.dauphine.blogger_box_backend.dto.PostDTO;
import com.dauphine.blogger_box_backend.exception.CategoryNameAlreadyExistsException;
import com.dauphine.blogger_box_backend.exception.CategoryNotFoundException;
import com.dauphine.blogger_box_backend.model.Category;
import com.dauphine.blogger_box_backend.model.Post;
import com.dauphine.blogger_box_backend.service.CategoryService;
import com.dauphine.blogger_box_backend.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.util.List;
import java.util.UUID;
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
    @Operation(
            summary = "Get all categories",
            description = "Retrieve all categories or filter like name"
    )
    public ResponseEntity<List<CategoryDTO>> getAll(@RequestParam(required = false) String name) {
        List<Category> categories = name == null || name.isBlank()
                ? categoryService.getAll()
                : categoryService.getAllLikeName(name);

        List<CategoryDTO> categoryDTOs = categories.stream()
                .map(category -> new CategoryDTO(category.getId(), category.getName()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(categoryDTOs);
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Get category by ID",
            description = "Retrieve a category by its ID"
    )
    @ApiResponse(responseCode = "200", description = "Category found")
    @ApiResponse(responseCode = "404", description = "Category not found")
    public ResponseEntity<CategoryDTO> getById(@PathVariable UUID id) throws CategoryNotFoundException {
        Category category = categoryService.getById(id);
        CategoryDTO categoryDTO = new CategoryDTO(category.getId(), category.getName());
        return ResponseEntity.ok(categoryDTO);
    }

    @PostMapping
    @Operation(
            summary = "Create a new category",
            description = "Create new category, only required field is the name of the category to create"
    )
    @ApiResponse(responseCode = "201", description = "Category created successfully")
    @ApiResponse(responseCode = "409", description = "Category name already exists")
    public ResponseEntity<CategoryDTO> create(@RequestBody CategoryDTO categoryDTO)
            throws CategoryNameAlreadyExistsException {

        Category category = categoryService.create(categoryDTO.getName());
        CategoryDTO createdCategoryDTO = new CategoryDTO(category.getId(), category.getName());

        return ResponseEntity
                .created(URI.create("/v1/categories/" + category.getId()))
                .body(createdCategoryDTO);
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Update a category",
            description = "Updates an existing category's name"
    )
    @ApiResponse(responseCode = "200", description = "Category updated successfully")
    @ApiResponse(responseCode = "404", description = "Category not found")
    public ResponseEntity<CategoryDTO> updateName(@PathVariable UUID id, @RequestBody CategoryDTO categoryDTO)
            throws CategoryNotFoundException {

        Category updatedCategory = categoryService.updateName(id, categoryDTO.getName());
        CategoryDTO updatedCategoryDTO = new CategoryDTO(updatedCategory.getId(), updatedCategory.getName());

        return ResponseEntity.ok(updatedCategoryDTO);
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Delete a category",
            description = "Deletes an existing category"
    )
    @ApiResponse(responseCode = "204", description = "Category deleted successfully")
    @ApiResponse(responseCode = "404", description = "Category not found")
    public ResponseEntity<Void> deleteCategory(@PathVariable UUID id) throws CategoryNotFoundException {
        categoryService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/posts")
    @Operation(
            summary = "Get posts by category",
            description = "Retrieves all posts that belong to a specific category"
    )
    @ApiResponse(responseCode = "200", description = "Posts retrieved successfully")
    @ApiResponse(responseCode = "404", description = "Category not found")
    public ResponseEntity<List<PostDTO>> getPostsByCategory(@PathVariable UUID id)
            throws CategoryNotFoundException {

        List<Post> posts = postService.getPostsByCategoryId(id);
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
}