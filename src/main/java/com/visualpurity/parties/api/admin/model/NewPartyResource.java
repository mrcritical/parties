package com.visualpurity.parties.api.admin.model;

import com.visualpurity.parties.api.model.media.PictureResource;
import com.visualpurity.parties.datastore.model.profile.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Singular;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NewPartyResource {
    @NotNull
    @Size(min = 2, max = 50)
    private String name;
    @Size(min = 2, max = 140)
    private String description;
    private LocalDateTime start;
    private LocalDateTime end;
    private PictureResource banner;
    @Singular
    private List<String> hostIds;
    private User createdBy;
}
