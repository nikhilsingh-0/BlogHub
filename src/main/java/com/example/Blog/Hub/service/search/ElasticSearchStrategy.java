package com.example.Blog.Hub.service.search;

import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import com.example.Blog.Hub.dto.*;
import com.example.Blog.Hub.entity.PostDocument;
import com.example.Blog.Hub.utills.SearchQueryBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ElasticSearchStrategy implements SearchStrategy {

    private final ElasticsearchOperations operations;

    @Override
    public PostResponse search(SearchRequestDTO request) {

        NativeQuery searchQuery = SearchQueryBuilder.searchQuery(request);

        SearchHits<PostDocument> hits = operations.search(searchQuery, PostDocument.class);

        List<PostDTO> posts = hits.getSearchHits().stream()
                .map(hit->mapToPostDTO(hit.getContent()))
                .toList();

        PostResponse response = new PostResponse();
        response.setContent(posts);
        response.setPageNumber(request.pageNumber());
        response.setPageSize(request.pageSize());
        response.setTotalElement((int) hits.getTotalHits());
        response.setTotalPages(
                (int) Math.ceil((double) hits.getTotalHits() / request.pageSize())
        );
        return response;
    }

    private PostDTO mapToPostDTO(PostDocument doc) {
        PostDTO dto = new PostDTO();
        dto.setId(doc.getId());
        dto.setTittle(doc.getTitle());
        dto.setContent(doc.getContent());
        return dto;
    }
}
