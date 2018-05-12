package com.visualpurity.parties.datastore.model;

import com.visualpurity.parties.datastore.model.profile.Guest;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.io.Serializable;

@Data
public class Attendee implements Serializable {
    private static final long serialVersionUID = 1370190535852732414L;
    private String id;
    @DBRef
    private Guest guest;
    private AttendeeStatus status;

    public enum AttendeeStatus {
        INVITATION_NOT_SENT,
        INVITED,
        ACCEPTED,
        DECLINED,
        JOINED,
        ATTENDED,
        NO_SHOW,
        LEFT_EARLY
    }
}
