package com.visualpurity.parties.datastore.model.profile;

import com.visualpurity.parties.datastore.model.media.Picture;

import java.io.Serializable;

public interface Profile extends Serializable {
    String getId();
    Name getName();
    Picture getAvatar();
}
