package com.visualpurity.parties.datastore;

import com.visualpurity.parties.datastore.model.Post;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface PostRepository extends ReactiveCrudRepository<Post, String> {

    Flux<Post> findAllByPostId(String postId);

    Flux<Post> findAllByPartyIdAndPostIdIsNull(String partyId);

}
