package com.visualpurity.parties.datastore;

import com.visualpurity.parties.datastore.model.Post;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@CacheConfig(cacheNames = "posts")
public interface PostRepository extends ReactiveCrudRepository<Post, String> {

    Flux<Post> findAllByPostId(String postId);

    Flux<Post> findAllByPartyIdAndPostIdIsNull(String partyId);

    @Override
    @CacheEvict
    Mono<Void> deleteById(String s);

    @Override
    @CacheEvict(key = "#entity.id")
    <S extends Post> Mono<S> save(S entity);

}
