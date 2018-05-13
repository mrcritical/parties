package com.visualpurity.parties.datastore.model.profile;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Name implements Serializable {
    private static final long serialVersionUID = 6252397841650377683L;
    private String first;
    private String last;
}
