package com.visualpurity.parties.listener.event;

import com.visualpurity.parties.api.model.ProfileResource;
import com.visualpurity.parties.listener.event.StartPartyEvent.PartyControlSource;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AttendeeGuestUpdatedEvent implements Serializable {
    private static final long serialVersionUID = 6798048068442414086L;
    private String id;
    private String partyId;
    private ProfileResource guest;
}
