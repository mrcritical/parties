package com.visualpurity.parties.api.model.guest;

import com.visualpurity.parties.api.model.guest.media.MediumResource;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class PostResource implements Serializable {
    private static final long serialVersionUID = 3228659660046599778L;
    private String text;
    private List<MediumResource> media;
    private String parentId;
}
