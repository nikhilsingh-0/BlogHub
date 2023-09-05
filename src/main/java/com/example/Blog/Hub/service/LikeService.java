package com.example.Blog.Hub.service;

public interface LikeService {

    String toggleLike(Long postId);
    boolean isLiked(Long postId);
}
