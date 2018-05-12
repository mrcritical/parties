package com.visualpurity.parties.api.model.guest;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class NameResource implements Serializable {
    private static final long serialVersionUID = -3273752606285329280L;
    private String first;
    private String last;
}
