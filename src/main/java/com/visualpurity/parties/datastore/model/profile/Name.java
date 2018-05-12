package com.visualpurity.parties.datastore.model.profile;

import lombok.Data;

import java.io.Serializable;

@Data
public class Name implements Serializable {
    private static final long serialVersionUID = 6252397841650377683L;
    private String first;
    private String last;
}
