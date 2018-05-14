package com.visualpurity.parties.api.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LikesResource implements Serializable {
    private static final long serialVersionUID = -506851457888815481L;
    private String id;
    private String postId;
    private List<ProfileResource> likes;
    private Integer count;
}
