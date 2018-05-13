package com.visualpurity.parties.datastore;

import com.visualpurity.parties.datastore.model.media.Picture;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface PictureRepository extends ReactiveCrudRepository<Picture, String> {

}
