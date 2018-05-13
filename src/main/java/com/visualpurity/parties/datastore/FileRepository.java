package com.visualpurity.parties.datastore;

import com.visualpurity.parties.datastore.model.media.File;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface FileRepository extends ReactiveCrudRepository<File, String> {

}
