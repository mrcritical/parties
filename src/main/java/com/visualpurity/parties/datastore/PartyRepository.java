package com.visualpurity.parties.datastore;

import com.visualpurity.parties.datastore.model.Party;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface PartyRepository extends ReactiveCrudRepository<Party, String> {

    Flux<Party> findAllByAccountId(String accountId);

}
