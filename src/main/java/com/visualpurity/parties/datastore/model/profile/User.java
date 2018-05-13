package com.visualpurity.parties.datastore.model.profile;

import com.visualpurity.parties.datastore.model.media.Picture;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Singular;
import org.springframework.data.mongodb.core.index.Indexed;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User implements Profile {
    private static final long serialVersionUID = 4068438201812265250L;
    private String id;
    private Name name;
    private Picture avatar;
    @Indexed
    private String userName;
    private String password;
    @Indexed
    private String emailAddress;
    @Singular
    private List<Role> roles;
    @Indexed
    private String accountId;
    private boolean enabled;

    public enum Role {
        ADMIN,
        BILLING,
        VIEWE_ONLY
    }

}
