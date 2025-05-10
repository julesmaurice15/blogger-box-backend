package com.dauphine.blogger_box_backend.service;

import com.dauphine.blogger_box_backend.exception.CategoryNotFoundException;
import com.dauphine.blogger_box_backend.exception.CategoryNameAlreadyExistsException;
import com.dauphine.blogger_box_backend.model.Category;
import com.dauphine.blogger_box_backend.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CategoryService {

    private final CategoryRepository repository;

    public CategoryService(CategoryRepository repository) {
        this.repository = repository;
    }

    public List<Category> getAll() {
        return repository.findAll();
    }

    public List<Category> getAllLikeName(String name) {
        return repository.findAllLikeName(name);
    }

    public Category getById(UUID id) throws CategoryNotFoundException {
        return repository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException(id));
    }

    public Category create(String name) throws CategoryNameAlreadyExistsException {
        // Vérifier si le nom existe déjà
        List<Category> existingCategories = getAllLikeName(name);
        for (Category cat : existingCategories) {
            if (cat.getName().equalsIgnoreCase(name)) {
                throw new CategoryNameAlreadyExistsException(name);
            }
        }

        Category category = new Category(name);
        return repository.save(category);
    }

    public Category updateName(UUID id, String name) throws CategoryNotFoundException {
        Category category = getById(id);
        category.setName(name);
        return repository.save(category);
    }

    public boolean deleteById(UUID id) throws CategoryNotFoundException {
        if (!repository.existsById(id)) {
            throw new CategoryNotFoundException(id);
        }
        repository.deleteById(id);
        return true;
    }
}
