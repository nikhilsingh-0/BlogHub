package com.example.Blog.Hub.service.search;

import com.example.Blog.Hub.dto.*;
import com.example.Blog.Hub.entity.Post;
import com.example.Blog.Hub.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DatabaseSearchStrategy implements SearchStrategy {

    private final PostRepository postRepository;
    private final ModelMapper modelMapper;

    @Override
    public PostResponse search(SearchRequestDTO request) {
        Pageable pageable = PageRequest.of(
                request.pageNumber(),
                request.pageSize(),
                buildSort(request)
        );
        Page<Post> page = postRepository.findPostByTittleContaining(request.query(),pageable);
        List<Post> posts = page.getContent();
        List<PostDTO> postList = posts.stream().map(post->modelMapper.map(post,PostDTO.class)).toList();

        PostResponse postResponse = new PostResponse();
        postResponse.setContent(postList);
        postResponse.setPageNumber(page.getNumber());
        postResponse.setPageSize(page.getSize());
        postResponse.setTotalElement(page.getNumberOfElements());
        postResponse.setTotalPages(page.getTotalPages());
        postResponse.setLastPages(page.isLast());
        return postResponse;
    }

    private Sort buildSort(SearchRequestDTO request) {

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
