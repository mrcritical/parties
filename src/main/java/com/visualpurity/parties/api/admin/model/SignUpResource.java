package com.visualpurity.parties.api.admin.model;

import com.visualpurity.parties.api.model.NameResource;
import lombok.Data;

import java.io.Serializable;

@Data
public class SignUpResource implements Serializable {
    private static final long serialVersionUID = -7620969118002066807L;
    private NameResource name;
    private String userName;
    private String password;
    private String emailAddress;
}
