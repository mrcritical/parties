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
public class UnlikeEvent implements Serializable {
    private static final long serialVersionUID = -8081639875298342466L;
    private String id;
    private String partyId;
    private String postId;
}
