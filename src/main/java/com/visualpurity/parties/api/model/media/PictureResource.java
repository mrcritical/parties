package com.visualpurity.parties.api.model.media;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PictureResource implements MediumResource {
    private static final long serialVersionUID = 3907588078642357854L;
    private String id;
    private String url;
    private Integer width;
    private Integer height;
    private String caption;
}
