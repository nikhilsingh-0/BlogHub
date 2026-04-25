package com.example.Blog.Hub.service.search;

import com.example.Blog.Hub.dto.PostResponse;
import com.example.Blog.Hub.dto.SearchRequestDTO;

import java.util.List;

public interface PostSearchService {

    PostResponse search(SearchRequestDTO searchRequestDTO);
    List<String> suggest(String prefix);
}
