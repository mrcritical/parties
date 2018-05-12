package com.visualpurity.parties.listener.event;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class LikeEvent implements Serializable {
    private static final long serialVersionUID = -8081639875298342466L;
    private String id;
    private String partyId;
    private String postId;
}
