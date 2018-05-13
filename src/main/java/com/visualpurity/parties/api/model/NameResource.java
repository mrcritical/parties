package com.visualpurity.parties.api.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NameResource implements Serializable {
    private static final long serialVersionUID = -3273752606285329280L;
    private String first;
    private String last;
}
