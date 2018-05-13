package com.visualpurity.parties.listener.event;

import com.visualpurity.parties.datastore.model.Attendee;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Singular;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PartyPostEvent implements Serializable {
    private static final long serialVersionUID = -2168975443423586612L;
    private String id;
    private String partyId;
    private LocalDateTime at;
    private Attendee by;
    private String text;
    @Singular("medium")
    private List<PostedMedium> media;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PostedMedium implements Serializable {
        private static final long serialVersionUID = 6023466783404498051L;
        private String type;
        private String url;
        private Integer width;
        private Integer height;
        private String caption;
        private String fileName;
        private String fileType;

    }

}
