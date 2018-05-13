package com.visualpurity.parties.listener.mapping;

import com.visualpurity.parties.api.model.CommentedResource;
import com.visualpurity.parties.api.model.PostedResource;
import com.visualpurity.parties.api.model.ProfileResource;
import com.visualpurity.parties.datastore.model.Post;
import com.visualpurity.parties.datastore.model.profile.Guest;
import com.visualpurity.parties.datastore.model.profile.User;
import com.visualpurity.parties.listener.event.PartyPostEvent;
import com.visualpurity.parties.listener.event.PostCommentEvent;
import ma.glasnost.orika.MapperFactory;
import net.rakugakibox.spring.boot.orika.OrikaMapperFactoryConfigurer;
import org.springframework.stereotype.Component;

@Component
public class EventMapping implements OrikaMapperFactoryConfigurer {
    @Override
    public void configure(MapperFactory mapperFactory) {
        mapperFactory.classMap(PartyPostEvent.class, Post.class)
                .field("by.guest", "by")
                .byDefault()
                .register();

        mapperFactory.classMap(PostCommentEvent.class, Post.class)
                .field("by.guest", "by")
                .byDefault()
                .register();

        mapperFactory.classMap(Post.class, PostedResource.class)
                .byDefault()
                .register();

        mapperFactory.classMap(Post.class, CommentedResource.class)
                .byDefault()
                .register();

        mapperFactory.classMap(Guest.class, ProfileResource.class)
                .byDefault()
                .register();

        mapperFactory.classMap(User.class, ProfileResource.class)
                .byDefault()
                .register();

    }
}
