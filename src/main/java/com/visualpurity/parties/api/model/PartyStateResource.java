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
public class PartyStateResource implements Serializable {
    private static final long serialVersionUID = -6608714389082437748L;
    private PartyResource.PartyStatus status;
}
