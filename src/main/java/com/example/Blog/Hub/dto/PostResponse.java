package com.example.Blog.Hub.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class PostResponse {
    int pageNumber;
    int pageSize;
    int totalElement;
    int totalPages;
    boolean lastPages;
    List<PostDTO> content;
}
