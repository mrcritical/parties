package com.visualpurity.parties.api.model.guest;

import lombok.Data;

import java.io.Serializable;

@Data
public class LikeResource implements Serializable {
    private static final long serialVersionUID = -3277822700288281924L;
    private String id;
    private String postId;
    private Integer likes;
}
