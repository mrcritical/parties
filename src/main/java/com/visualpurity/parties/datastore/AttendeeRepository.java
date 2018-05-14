package com.visualpurity.parties.datastore;

import com.visualpurity.parties.datastore.model.Attendee;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@CacheConfig(cacheNames = "attendees")
public interface AttendeeRepository extends ReactiveCrudRepository<Attendee, String> {

    Flux<Attendee> findAllByPartyId(String partyId);

    @Override
    @CacheEvict
    Mono<Void> deleteById(String s);

    @Override
    @CacheEvict(key = "#entity.id")
    <S extends Attendee> Mono<S> save(S entity);
}
