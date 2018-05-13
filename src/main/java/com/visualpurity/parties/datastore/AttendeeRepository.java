package com.visualpurity.parties.datastore;

import com.visualpurity.parties.datastore.model.Attendee;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface AttendeeRepository extends ReactiveCrudRepository<Attendee, String> {

    Flux<Attendee> findAllByPartyId(String partyId);

}
