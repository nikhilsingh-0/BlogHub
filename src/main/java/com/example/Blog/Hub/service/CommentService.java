package com.example.Blog.Hub.service;

import com.example.Blog.Hub.dto.CommentDTO;

import java.util.List;

public interface CommentService {

    CommentDTO createComment(CommentDTO commentDTO,Long postId);
    CommentDTO getCommentById(Long id);
    CommentDTO updateComment(CommentDTO commentDTO);
    String deleteCommentById(Long id);
    List<CommentDTO> getCommentByPostId(Long id);
}
