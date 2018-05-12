package com.visualpurity.parties.datastore.model.profile;

import lombok.Data;

import java.io.Serializable;

@Data
public class PhoneNumber implements Serializable {
    private static final long serialVersionUID = 7207014521557551702L;
    private String id;
    private String type;
    private String number;
}
