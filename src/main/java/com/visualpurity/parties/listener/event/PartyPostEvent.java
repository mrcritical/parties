package com.visualpurity.parties.listener.event;

import com.visualpurity.parties.api.model.guest.media.MediumResource;
import com.visualpurity.parties.datastore.model.Attendee;
import lombok.Builder;
import lombok.Data;
import lombok.Singular;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class PartyPostEvent implements Serializable {
    private static final long serialVersionUID = -2168975443423586612L;
    private String id;
    private String partyId;
    private LocalDateTime at;
    private Attendee by;
    private String text;
    @Singular("medium")
    private List<MediumResource> media;
}
