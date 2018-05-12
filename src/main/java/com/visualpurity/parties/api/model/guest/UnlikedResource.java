package com.visualpurity.parties.api.model.guest;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class UnlikedResource implements Serializable {
    private static final long serialVersionUID = -3277822700288281924L;
    private String id;
    private String postId;
    private Integer likes;
}
