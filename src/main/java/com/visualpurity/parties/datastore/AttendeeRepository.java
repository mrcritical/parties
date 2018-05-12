package com.visualpurity.parties.datastore;

import com.visualpurity.parties.datastore.model.Attendee;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface AttendeeRepository extends ReactiveCrudRepository<Attendee, String> {
}
