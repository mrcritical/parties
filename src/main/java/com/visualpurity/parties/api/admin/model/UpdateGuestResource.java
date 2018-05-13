package com.visualpurity.parties.api.admin.model;

import com.visualpurity.parties.api.model.NameResource;
import com.visualpurity.parties.api.model.ProfileResource;
import com.visualpurity.parties.api.model.media.PictureResource;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateGuestResource {
    private NameResource name;
    private PictureResource avatar;
}
