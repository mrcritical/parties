package com.visualpurity.parties.datastore.model;

import com.visualpurity.parties.datastore.model.profile.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Creator implements Serializable {
    private static final long serialVersionUID = 7405288005143883556L;
    private LocalDateTime at;
    @DBRef
    private User user;
}
