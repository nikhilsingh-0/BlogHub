package com.example.Blog.Hub.repository;

import com.example.Blog.Hub.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post,Long> {

    List<Post> findPostByUserId(Long id);
    Page<Post> findPostByCategoryId(Long id,Pageable pageable);
    Page<Post> findPostByTittleContaining(String query, Pageable pageable);


}
