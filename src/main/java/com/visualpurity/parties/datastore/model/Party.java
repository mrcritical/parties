package com.visualpurity.parties.datastore.model;

import com.visualpurity.parties.datastore.model.media.Medium;
import com.visualpurity.parties.datastore.model.schedule.ScheduledEvent;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class Party implements Serializable {
    private static final long serialVersionUID = -6160322727674069918L;
    private String id;
    private String name;
    private String description;
    private Creator createdBy;
    private LocalDateTime start;
    private LocalDateTime end;
    private Medium banner;
    private List<String> hosts;
    private List<Attendee> attendees;
    private List<ScheduledEvent> scheduledEvents;
    private List<String> gallery;
    private PartyStatus status;
    private String accountId;

    public enum PartyStatus {
        NOT_STARTED,
        IN_PROGRESS,
        ENDED
    }
}
