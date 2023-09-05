package com.example.Blog.Hub.controller;

import com.example.Blog.Hub.dto.CommentDTO;
import com.example.Blog.Hub.service.CommentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CommentController {


    @Autowired
    CommentService service;

    @PostMapping("/post/{postId}/comment")
    public ResponseEntity<CommentDTO> createComment(@Valid @RequestBody CommentDTO commentDTO, @PathVariable Long postId){
        CommentDTO commentDTO1 =  service.createComment(commentDTO,postId);
        return new ResponseEntity<>(commentDTO1, HttpStatus.CREATED);
    }

    @GetMapping("/post/{postId}/comment/{commentId}")
    public ResponseEntity<CommentDTO> getComment(@PathVariable Long commentId){
        CommentDTO commentDTO1 =  service.getCommentById(commentId);
        return new ResponseEntity<>(commentDTO1, HttpStatus.CREATED);
    }

    @GetMapping("/post/{postId}/comments")
    public ResponseEntity<List<CommentDTO>> getAllCommentByPostId(@PathVariable Long postId){
        List<CommentDTO> commentDTO1 =  service.getCommentByPostId(postId);
        return ResponseEntity.ok(commentDTO1);
    }

}
