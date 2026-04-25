package com.example.Blog.Hub.service.search;

import com.example.Blog.Hub.dto.PostResponse;
import com.example.Blog.Hub.dto.SearchRequestDTO;

public interface SearchStrategy {
    PostResponse search(SearchRequestDTO searchRequestDTO);
}
