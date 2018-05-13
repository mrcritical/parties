package com.visualpurity.parties.api.model.media;

import lombok.Data;

@Data
public class VideoResource implements MediumResource {
    private static final long serialVersionUID = -5027090858594403640L;
    private String id;
    private String url;
}
