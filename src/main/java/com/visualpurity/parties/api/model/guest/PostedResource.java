package com.visualpurity.parties.api.model.guest;

import com.visualpurity.parties.api.model.guest.media.MediumResource;
import lombok.Builder;
import lombok.Data;
import lombok.Singular;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class PostedResource implements Serializable {
    private static final long serialVersionUID = 3034886868475544787L;
    private String id;
    private LocalDateTime at;
    private LocalDateTime modifiedAt;
    private ProfileResource by;
    private String text;
    @Singular("medium")
    private List<MediumResource> media;
    @Singular
    private List<PostedResource> comments;
    private Integer likes;
}
