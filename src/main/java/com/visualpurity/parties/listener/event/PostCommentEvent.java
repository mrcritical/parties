package com.visualpurity.parties.listener.event;

import com.visualpurity.parties.datastore.model.Attendee;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
public class PostCommentEvent implements Serializable {
    private static final long serialVersionUID = 7529973119165885950L;
    private String id;
    private String partyId;
    private LocalDateTime at;
    private Attendee by;
    private String text;
    private String postId;
}
