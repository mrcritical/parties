package com.visualpurity.parties.api.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AttendeeResource implements Serializable {
    private static final long serialVersionUID = 1370190535852732414L;
    private String id;
    private ProfileResource guest;
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
