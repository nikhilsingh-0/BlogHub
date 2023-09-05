package com.example.Blog.Hub.controller;

import com.example.Blog.Hub.dto.PostDTO;
import com.example.Blog.Hub.dto.PostResponse;
import com.example.Blog.Hub.service.PostService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
public class PostController {

    @Autowired
    PostService service;

    @PostMapping(value = "/posts/{categoryId}/{userId}",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<PostDTO> createPost(@Valid @RequestPart PostDTO postDTO, @PathVariable Long userId, @PathVariable Long categoryId, @RequestPart("image") MultipartFile image) throws IOException {
        PostDTO post = service.createPost(postDTO,userId,categoryId,image);
        return new ResponseEntity<>(post, HttpStatus.CREATED);
    }

    @GetMapping("/posts/{postId}")
    public ResponseEntity<PostDTO> getPost(@PathVariable Long postId){
        return ResponseEntity.ok(service.getPostById(postId));
    }

    @GetMapping("/posts")
    public ResponseEntity<PostResponse> getAllPost(@RequestParam(value = "pageNumber",defaultValue = "0",required = false) int pageNumber,
                                                   @RequestParam(value = "pageSize",defaultValue = "10",required = false) int pageSize,
                                                   @RequestParam(value = "sort",defaultValue = "date",required = false) String sort,
                                                   @RequestParam(value = "category",defaultValue = "-1",required = false) Long categoryId){
        return ResponseEntity.ok(service.getAllPost(pageNumber,pageSize,sort,categoryId));
    }

    @GetMapping("posts/user/{userId}")
    public ResponseEntity<List<PostDTO>> getPostByUserId(@PathVariable Long userId){
        List<PostDTO> postDTO = service.getPostsByUserId(userId);
        return ResponseEntity.ok(postDTO);
    }

    @PutMapping(value = "/posts",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<PostDTO> updatePost( @Valid @RequestPart PostDTO postDTO,@RequestParam("image") MultipartFile image){
        PostDTO post = service.updatePost(postDTO, image);
        return ResponseEntity.ok(post);
    }

    @GetMapping("/posts/search/{query}")
    public ResponseEntity<PostResponse> searchPost(@PathVariable String query, @RequestParam(value = "pageNumber",defaultValue = "0",required = false) int pageNumber,
                                                   @RequestParam(value = "pageSize",defaultValue = "10",required = false) int pageSize,
                                                   @RequestParam(value = "sort",defaultValue = "date",required = false) String sort){
        PostResponse postResponse = service.searchPost(query,pageNumber,pageSize,sort);
        return ResponseEntity.ok(postResponse);
    }

    @DeleteMapping("/posts/{postId}")
    public ResponseEntity<String> deletePost(@PathVariable Long postId){
        String result = service.deletePostById(postId);
        return ResponseEntity.ok(result);
    }


}
