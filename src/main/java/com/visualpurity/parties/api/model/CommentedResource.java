package com.visualpurity.parties.api.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentedResource implements Serializable {
    private static final long serialVersionUID = -5168513172978400297L;
    private String id;
    private LocalDateTime at;
    private LocalDateTime modifiedAt;
    private ProfileResource by;
    private String text;
    private Integer likes;
    private String postId;
}
