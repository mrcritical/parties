package com.visualpurity.parties.datastore.model;

import com.visualpurity.parties.datastore.model.media.Medium;
import com.visualpurity.parties.datastore.model.profile.Profile;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Singular;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Post implements Serializable {
    private static final long serialVersionUID = 3034886868475544787L;
    private String id;
    private LocalDateTime at;
    private LocalDateTime modifiedAt;
    @DBRef
    private Profile by;
    private String text;
    @DBRef
    @Singular("medium")
    private List<Medium> media;
    private Integer likes;
    @Indexed
    private String partyId;
    @Indexed
    private String postId;
}
