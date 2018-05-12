package com.visualpurity.parties.datastore.model.media;

import lombok.Data;

@Data
public class File implements Medium {
    private static final long serialVersionUID = -2396563297827451466L;
    private String id;
    private String url;
    private String name;
    private String type;
}
