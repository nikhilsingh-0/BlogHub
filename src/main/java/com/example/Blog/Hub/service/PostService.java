package com.example.Blog.Hub.service;

import com.example.Blog.Hub.dto.PostDTO;
import com.example.Blog.Hub.dto.PostResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface PostService {

    PostDTO createPost(PostDTO postDTO, Long userId, Long categoryId,MultipartFile image);
    PostDTO getPostById(Long id);
    PostResponse getAllPost(int pageNumber, int pageSize,String sort,Long categoryId);
    PostDTO updatePost(PostDTO postDTO,MultipartFile image);
    String deletePostById(Long id);
    List<PostDTO> getPostsByUserId(Long id);
    PostResponse searchPost(String query,int pageNumber,int pageSize,String sort);


}
