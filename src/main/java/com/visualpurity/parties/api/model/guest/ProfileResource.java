package com.visualpurity.parties.api.model.guest;

import com.visualpurity.parties.api.model.guest.media.PictureResource;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class ProfileResource implements Serializable {
    private static final long serialVersionUID = 7380563794621924215L;
    private String id;
    private NameResource name;
    private PictureResource avatar;
}
