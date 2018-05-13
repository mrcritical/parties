package com.visualpurity.parties.datastore;

import com.visualpurity.parties.datastore.model.media.Video;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface VideoRepository extends ReactiveCrudRepository<Video, String> {

}
