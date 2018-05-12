package com.visualpurity.parties.datastore.model.media;

import lombok.Data;

@Data
public class Video implements Medium {
    private static final long serialVersionUID = -5027090858594403640L;
    private String id;
    private String url;
}
