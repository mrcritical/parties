package com.visualpurity.parties.datastore.model.profile;

import lombok.Data;

import java.io.Serializable;

@Data
public class EmailAddress implements Serializable {
    private static final long serialVersionUID = -1023674699503017757L;
    private String id;
    private String type;
    private String address;
}
