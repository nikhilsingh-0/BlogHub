package com.example.Blog.Hub.event;

import com.example.Blog.Hub.entity.Post;
import com.example.Blog.Hub.entity.PostDocument;
import com.example.Blog.Hub.repository.PostSearchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.elasticsearch.core.suggest.Completion;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.List;

@RequiredArgsConstructor
@Component
public class PostSearchEventListener {

    private final PostSearchRepository searchRepository;

    @Retryable(
            retryFor = Exception.class,
            maxAttempts = 3,
            backoff = @Backoff(delay = 2000)
    )
    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handlePostCreated(PostCreatedEvent event){
        searchRepository.save(postToPostDocument(event.post()));
    }

    @Retryable(
            retryFor = Exception.class,
            maxAttempts = 3,
            backoff = @Backoff(delay = 2000)
    )
    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handlePostUpdated(PostUpdatedEvent event) {
        searchRepository.save(postToPostDocument(event.post()));
    }

    @Retryable(
            retryFor = Exception.class,
            maxAttempts = 3,
            backoff = @Backoff(delay = 2000)
    )
    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handlePostDeleted(PostDeletedEvent event) {
        searchRepository.deleteById(event.postId());
    }



    private PostDocument postToPostDocument(Post post){
        PostDocument doc = new PostDocument();
        doc.setId(post.getId());
        doc.setTitle(post.getTittle());
        doc.setContent(post.getContent());
        doc.setAuthor(post.getUser().getName());
        doc.setCategory(post.getCategory().getCategoryTittle());
        doc.setCreatedAt(post.getDate());
        doc.setLikeCount(post.getLikeCount());
        doc.setSuggest(buildSuggest(post));
        return doc;
    }

    private Completion buildSuggest(Post post) {
        return new Completion(
                List.of(post.getTittle(),
                        post.getCategory().getCategoryTittle(),
                        post.getUser().getName()
                ));
    }

}
