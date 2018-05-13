package com.visualpurity.parties.api.model;

import com.visualpurity.parties.api.model.media.PostMediumResource;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class PostResource implements Serializable {
    private static final long serialVersionUID = 3228659660046599778L;
    private String text;
    private List<PostMediumResource> media;
}
