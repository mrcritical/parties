package com.visualpurity.parties.api.admin.model;

import com.visualpurity.parties.api.model.ProfileResource;
import com.visualpurity.parties.api.model.media.PictureResource;
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
public class UpdatePartyResource {
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
}
