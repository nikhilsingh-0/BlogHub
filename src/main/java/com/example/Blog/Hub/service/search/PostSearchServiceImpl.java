package com.example.Blog.Hub.service.search;

import co.elastic.clients.elasticsearch._types.ElasticsearchException;
import com.example.Blog.Hub.dto.PostResponse;
import com.example.Blog.Hub.dto.SearchRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostSearchServiceImpl implements PostSearchService {

    private final ElasticSearchStrategy elasticSearchStrategy;
    private final DatabaseSearchStrategy databaseSearchStrategy;
    private final SuggestService suggestService;

    @Override
    public PostResponse search(SearchRequestDTO searchRequestDTO) {
        try {
            return elasticSearchStrategy.search(searchRequestDTO);
        }catch (ElasticsearchException | DataAccessException ex){
            return databaseSearchStrategy.search(searchRequestDTO);
        }
    }

    @Override
    public List<String> suggest(String prefix) {
        return suggestService.suggest(prefix);
    }
}
