package com.visualpurity.parties.api.model;

import com.visualpurity.parties.api.model.media.MediumResource;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class PartyResource implements Serializable {
    private static final long serialVersionUID = -6800888202414801455L;
    private String id;
    private String name;
    private String description;
    private LocalDateTime start;
    private LocalDateTime end;
    private MediumResource banner;
    private List<ProfileResource> hosts;
    private List<AttendeeResource> attendees;
    private List<PostResource> posts;
    private List<MediumResource> gallery;
    private PartyStatus status;

    public enum PartyStatus {
        NOT_STARTED,
        IN_PROGRESS,
        ENDED
    }
}
