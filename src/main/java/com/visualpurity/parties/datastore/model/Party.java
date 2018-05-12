package com.visualpurity.parties.datastore.model;

import com.visualpurity.parties.datastore.model.media.Medium;
import com.visualpurity.parties.datastore.model.profile.Profile;
import com.visualpurity.parties.datastore.model.schedule.ScheduledEvent;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.DBRef;

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
    @DBRef
    private List<Profile> hosts;
    private List<Attendee> attendees;
    private List<ScheduledEvent> scheduledEvents;
    @DBRef
    private List<Post> posts;
    @DBRef
    private List<Medium> gallery;
    private PartyStatus status;
    @DBRef
    private Account account;

    public enum PartyStatus {
        NOT_STARTED,
        IN_PROGRESS,
        ENDED
    }
}
