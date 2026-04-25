package com.example.Blog.Hub.service.search;

import co.elastic.clients.elasticsearch.core.search.Suggester;
import com.example.Blog.Hub.entity.PostDocument;
import lombok.RequiredArgsConstructor;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.FetchSourceFilter;
import org.springframework.data.elasticsearch.core.suggest.response.Suggest;
import org.springframework.stereotype.Component;
import java.util.List;

@RequiredArgsConstructor
@Component
public class SuggestService {

    private final ElasticsearchOperations operations;

    public List<String> suggest(String prefix) {

        Suggester suggester = Suggester.of(s -> s
                .suggesters("post-suggest", fs -> fs
                                .prefix(prefix)
                                .completion(c -> c
                                        .field("suggest")
                                        .size(10))));

        NativeQuery nativeQuery = NativeQuery.builder()
                .withSuggester(suggester)
                .withMaxResults(0)
                .withSourceFilter(
                        FetchSourceFilter.of(b -> b.withExcludes("*"))
                )
                .build();

        SearchHits<PostDocument> hits =
                operations.search(nativeQuery, PostDocument.class);

        Suggest suggest = hits.getSuggest();
        if (suggest == null || suggest.getSuggestion("post-suggest") == null) {
            return List.of();
        }

        return suggest.getSuggestion("post-suggest")
                .getEntries()
                .stream()
                .flatMap(e -> e.getOptions().stream())
                .map(Suggest.Suggestion.Entry.Option::getText)
                .distinct()
                .toList();
    }

}
