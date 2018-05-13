package com.visualpurity.parties.listener.event;

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
public class EndPartyEvent implements Serializable {
    private static final long serialVersionUID = -8268416937026289527L;
    private String partyId;
    private PartyControlSource type;
}
