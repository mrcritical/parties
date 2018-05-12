package com.visualpurity.parties.datastore.model.schedule;

import com.visualpurity.parties.datastore.model.media.Medium;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class ScheduledAction implements Serializable {
    private static final long serialVersionUID = -4320260577124669741L;
    private String text;
    private List<Medium> media;
}
