package com.dauphine.blogger_box_backend.repository;

import com.dauphine.blogger_box_backend.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface CategoryRepository extends JpaRepository<Category, UUID> {

    @Query("""
            SELECT category 
            FROM Category category
            WHERE UPPER(category.name) LIKE UPPER(CONCAT('%', :name ,'%'))
           """)
    List<Category> findAllLikeName(@Param("name") String name);
}
