package com.example.Blog.Hub.dto;

public record SearchRequestDTO(
        String query,
        String category,
        SortBy sortBy,
        SortDir sortDir,
        Integer pageNumber,
        Integer pageSize
) {
    public SearchRequestDTO{
        if (sortBy == null) sortBy = SortBy.RELEVANCE;
        if (sortDir == null) sortDir = SortDir.DESC;
        if (pageNumber == null ||pageNumber < 0) pageNumber = 0;
        if (pageSize == null|| pageSize <= 0 ) pageSize = 10;
    }
}