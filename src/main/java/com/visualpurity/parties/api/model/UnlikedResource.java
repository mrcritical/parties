package com.visualpurity.parties.api.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UnlikedResource implements Serializable {
    private static final long serialVersionUID = -3277822700288281924L;
    private String id;
    private String postId;
    private Integer likes;
}
