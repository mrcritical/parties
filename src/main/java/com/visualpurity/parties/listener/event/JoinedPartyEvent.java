package com.visualpurity.parties.listener.event;

import com.visualpurity.parties.datastore.model.Attendee;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
public class JoinedPartyEvent implements Serializable {
    private static final long serialVersionUID = 7737516891298886556L;
    private String partyId;
    private Attendee who;
    private LocalDateTime at;
}
