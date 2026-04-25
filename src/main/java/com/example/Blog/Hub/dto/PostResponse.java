package com.example.Blog.Hub.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class PostResponse {
    private int pageNumber;
    private int pageSize;
    private int totalElement;
    private int totalPages;
    private boolean lastPages;
    private List<PostDTO> content;
}
