package com.example.Blog.Hub.controller;

import com.example.Blog.Hub.dto.PostResponse;
import com.example.Blog.Hub.dto.SearchRequestDTO;
import com.example.Blog.Hub.service.search.PostSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostSearchController {

    private final PostSearchService searchService;

    @GetMapping("/search")
    public ResponseEntity<PostResponse> postSearch(@ModelAttribute SearchRequestDTO searchRequestDTO){
        return ResponseEntity.ok(searchService.search(searchRequestDTO));
    }

    @GetMapping("/suggest")
    public List<String> postSearchSuggest(@RequestParam(value = "suggest",required = true) String prefix){
        return searchService.suggest(prefix);
    }
}
