package com.example.Blog.Hub.repository;

import com.example.Blog.Hub.entity.Like;
import jakarta.persistence.NamedQuery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<Like,Long> {

    Optional<Like> findByPostIdAndUserId(Long postId,Long userId);
}
