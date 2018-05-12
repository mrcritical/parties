package com.visualpurity.parties.datastore.model;

import com.visualpurity.parties.datastore.model.media.Medium;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.io.Serializable;
import java.util.List;

@Data
public class Account implements Serializable {
    private static final long serialVersionUID = -583641567243827252L;
    private String id;
    private String name;
    private String timeZone;
    @DBRef
    private List<Medium> assets;
}
