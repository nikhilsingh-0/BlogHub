package com.example.Blog.Hub.controller;

import com.example.Blog.Hub.service.LikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
public class LikeController {

    @Autowired
    private LikeService likeService;

    @PostMapping("posts/{postId}/like")
    public ResponseEntity<String> toggleLike(@PathVariable Long postId){
        return ResponseEntity.ok(likeService.toggleLike(postId));
    }

    @GetMapping("/posts/{postId}/like")
    public ResponseEntity<Boolean> isLiked(@PathVariable Long postId){
        return ResponseEntity.ok(likeService.isLiked(postId));
    }

}
