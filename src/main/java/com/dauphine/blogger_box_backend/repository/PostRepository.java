package com.dauphine.blogger_box_backend.repository;

import com.dauphine.blogger_box_backend.model.Post;
import com.dauphine.blogger_box_backend.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface PostRepository extends JpaRepository<Post, UUID> {

    List<Post> findByCategoryOrderByCreatedDateDesc(Category category);

    List<Post> findByCreatedDateOrderByCreatedDateDesc(LocalDateTime date);

    @Query("""
            SELECT p FROM Post p
            WHERE UPPER(p.title) LIKE UPPER(CONCAT('%', :value, '%'))
            OR UPPER(p.content) LIKE UPPER(CONCAT('%', :value, '%'))
            ORDER BY p.createdDate DESC
           """)
    List<Post> findByTitleOrContentContaining(@Param("value") String value);
}
