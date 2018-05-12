package com.visualpurity.parties.api.model.guest.media;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PictureResource implements MediumResource {
    private static final long serialVersionUID = 3907588078642357854L;
    private String id;
    private String url;
    private Integer width;
    private Integer height;
    private String caption;
}
