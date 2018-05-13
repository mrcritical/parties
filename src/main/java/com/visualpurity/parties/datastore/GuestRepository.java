package com.visualpurity.parties.datastore;

import com.visualpurity.parties.datastore.model.profile.Guest;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface GuestRepository extends ReactiveCrudRepository<Guest, String> {

    Flux<Guest> findAllByAccountId(String accountId);

}
