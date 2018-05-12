package com.visualpurity.parties.datastore;

import com.visualpurity.parties.datastore.model.Party;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface PartyRepository extends ReactiveCrudRepository<Party, String> {
}
