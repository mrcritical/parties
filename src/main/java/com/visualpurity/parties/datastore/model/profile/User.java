package com.visualpurity.parties.datastore.model.profile;

import com.visualpurity.parties.datastore.model.Account;
import com.visualpurity.parties.datastore.model.media.Picture;
import lombok.Data;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.util.List;

@Data
public class User implements Profile {
    private static final long serialVersionUID = 4068438201812265250L;
    private String id;
    private Name name;
    private Picture avatar;
    @Indexed
    private String userName;
    private String password;
    private List<String> roles;
    @DBRef
    private Account account;
}
