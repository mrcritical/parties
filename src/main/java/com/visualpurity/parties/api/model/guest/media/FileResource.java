package com.visualpurity.parties.api.model.guest.media;

import lombok.Data;

@Data
public class FileResource implements MediumResource {
    private static final long serialVersionUID = -2396563297827451466L;
    private String id;
    private String url;
    private String name;
    private String type;
}
