package com.example.Blog.Hub.repository;

import com.example.Blog.Hub.entity.PostDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface PostSearchRepository extends ElasticsearchRepository<PostDocument,Long> {
}
