package com.visualpurity.parties.api.model.media;

import lombok.Data;

import java.io.Serializable;

@Data
public class PostMediumResource implements Serializable {
    private static final long serialVersionUID = -6909274660826550621L;
    private String type;
    private String url;
    private Integer width;
    private Integer height;
    private String caption;
    private String fileName;
    private String fileType;
}
