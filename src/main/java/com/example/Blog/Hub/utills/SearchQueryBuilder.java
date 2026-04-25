package com.example.Blog.Hub.utills;

import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import com.example.Blog.Hub.dto.SearchRequestDTO;
import com.example.Blog.Hub.dto.SortBy;
import com.example.Blog.Hub.dto.SortDir;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;

public class SearchQueryBuilder {

    public static NativeQuery searchQuery(SearchRequestDTO request){
        Pageable pageable = PageRequest.of(
                request.pageNumber(),
                request.pageSize(),
                buildSort(request)
        );

        Query query = buildQuery(request);


        return NativeQuery.builder()
                .withQuery(query)
                .withPageable(pageable)
                .build();
    }

    private static Query buildQuery(SearchRequestDTO request) {

        return Query.of(query -> query.bool(builder -> {

            //Full-text search
            if (request.query() != null && !request.query().isBlank()) {
                builder.must(m -> m.multiMatch(mm -> mm
                        .query(request.query())
                        .fields("title^2", "content")
                ));
            } else {
                builder.must(m -> m.matchAll(ma -> ma));
            }

            //Category filter
            if (request.category() != null && !request.category().isBlank()) {
                builder.filter(f -> f.term(t -> t
                        .field("category")
                        .value(request.category())
                ));
            }

            return builder;
        }));
    }

    private static Sort buildSort(SearchRequestDTO request) {

        if (request.sortBy() == SortBy.RELEVANCE) {
            return Sort.unsorted();
        }

        String field = switch (request.sortBy()) {
            case RECENT -> "createdAt";
            case POPULAR -> "likeCount";
            default -> "createdAt";
        };

        Sort sort = Sort.by(field);
        return request.sortDir() == SortDir.DESC
                ? sort.descending()
                : sort.ascending();
    }
}
