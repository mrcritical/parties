package com.visualpurity.parties.api.model;

import com.visualpurity.parties.api.model.media.MediumResource;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Singular;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
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
