package com.visualpurity.parties.datastore.model.media;

import lombok.Data;

@Data
public class Picture implements Medium {
    private static final long serialVersionUID = 3907588078642357854L;
    private String id;
    private String url;
    private Integer width;
    private Integer height;
    private String caption;
}
