package com.visualpurity.parties.api.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class CommentResource implements Serializable {
    private static final long serialVersionUID = -447509157684891233L;
    private String text;
    private String postId;
}
