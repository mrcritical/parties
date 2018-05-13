package com.visualpurity.parties.datastore.model;

import com.visualpurity.parties.datastore.model.media.Medium;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Account implements Serializable {
    private static final long serialVersionUID = -583641567243827252L;
    private String id;
    private String name;
    private String timeZone;
    private LocalDateTime cancelledAt;
    @DBRef
    private List<Medium> assets;
}
