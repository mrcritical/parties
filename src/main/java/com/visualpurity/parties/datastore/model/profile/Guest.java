package com.visualpurity.parties.datastore.model.profile;

import com.visualpurity.parties.datastore.model.media.Picture;
import lombok.Data;

import java.util.List;

@Data
public class Guest implements Profile {
    private static final long serialVersionUID = 7380563794621924215L;
    private String id;
    private Name name;
    private Picture avatar;
    private List<EmailAddress> emailAddresses;
    private List<PhoneNumber> phoneNumbers;
    private String accountId;
}
