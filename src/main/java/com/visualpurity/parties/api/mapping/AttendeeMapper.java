package com.visualpurity.parties.api.mapping;

import com.visualpurity.parties.api.model.AttendeeResource;
import com.visualpurity.parties.api.model.ProfileResource;
import com.visualpurity.parties.datastore.model.Attendee;
import com.visualpurity.parties.datastore.model.profile.Guest;
import ma.glasnost.orika.MapperFactory;
import net.rakugakibox.spring.boot.orika.OrikaMapperFactoryConfigurer;
import org.springframework.stereotype.Component;

@Component
public class AttendeeMapper implements OrikaMapperFactoryConfigurer {
    @Override
    public void configure(MapperFactory mapperFactory) {
        mapperFactory.classMap(Attendee.class, ProfileResource.class)
                .field("guest.name", "name")
                .field("guest.avatar.url", "avatar.url")
                .byDefault()
                .register();

        mapperFactory.classMap(Guest.class, ProfileResource.class)
                .byDefault()
                .register();

        mapperFactory.classMap(Attendee.class, AttendeeResource.class)
                .byDefault()
                .register();
    }
}
