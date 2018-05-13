package com.visualpurity.parties.datastore.model.schedule;

import com.visualpurity.parties.datastore.model.profile.Profile;
import lombok.Data;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class ScheduledEvent implements Serializable {
    private static final long serialVersionUID = 6103336541688234892L;
    private String id;
    private LocalDateTime at;
    @DBRef
    private Profile by;
    private ScheduledAction action;
    @Indexed
    private String partyId;
}
