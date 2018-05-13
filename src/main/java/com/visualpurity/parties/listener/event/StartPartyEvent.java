package com.visualpurity.parties.listener.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StartPartyEvent implements Serializable {
    private static final long serialVersionUID = -8268416937026289527L;
    private String partyId;
    private PartyControlSource type;

    public enum PartyControlSource {
        MANUAL,
        AUTOMATIC
    }
}
