package com.visualpurity.parties.datastore;

import com.visualpurity.parties.datastore.model.Account;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface AccountRepository extends ReactiveCrudRepository<Account, String> {

}
