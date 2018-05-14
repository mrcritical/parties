package com.visualpurity.parties.listener;

import com.visualpurity.parties.api.model.AttendeeResource;
import com.visualpurity.parties.api.model.CommentedResource;
import com.visualpurity.parties.api.model.LikedResource;
import com.visualpurity.parties.api.model.PartyResource.PartyStatus;
import com.visualpurity.parties.api.model.PartyStateResource;
import com.visualpurity.parties.api.model.PostedResource;
import com.visualpurity.parties.cache.Cached;
import com.visualpurity.parties.datastore.AttendeeRepository;
import com.visualpurity.parties.datastore.PostRepository;
import com.visualpurity.parties.datastore.model.Attendee;
import com.visualpurity.parties.datastore.model.Post;
import com.visualpurity.parties.listener.event.AttendeeGuestUpdatedEvent;
import com.visualpurity.parties.listener.event.EndPartyEvent;
import com.visualpurity.parties.listener.event.JoinedPartyEvent;
import com.visualpurity.parties.listener.event.LeftPartyEvent;
import com.visualpurity.parties.listener.event.LikeEvent;
import com.visualpurity.parties.listener.event.PartyPostEvent;
import com.visualpurity.parties.listener.event.PostCommentEvent;
import com.visualpurity.parties.listener.event.StartPartyEvent;
import com.visualpurity.parties.listener.event.UnlikeEvent;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import ma.glasnost.orika.MapperFacade;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.lang.String.format;

@Component
@RequiredArgsConstructor
public class PartyEventListener {

    @NonNull
    private final AttendeeRepository attendeeRepository;

    @NonNull
    private final PostRepository postRepository;

    @NonNull
    private final SimpMessageSendingOperations messagingTemplate;

    @NonNull
    private final MapperFacade mapperFacade;

    @NonNull
    private final Cached cached;

    @Async
    @EventListener(PartyPostEvent.class)
    public void handleEvent(PartyPostEvent event) {
        Post post = mapperFacade.map(event, Post.class);
        post.setLikes(new ArrayList<>());
        postRepository
                .save(post)
                .doOnNext(post1 -> {
                    // Send the update to the party
                    messagingTemplate.convertAndSend(
                            format("/party/%s",
                                    event.getPartyId()),
                            mapperFacade.map(post1, PostedResource.class)
                    );
                })
                .subscribe();
    }

    @Async
    @EventListener(PostCommentEvent.class)
    public void handleEvent(PostCommentEvent event) {
        Post comment = mapperFacade.map(event, Post.class);
        comment.setLikes(new ArrayList<>());
        postRepository
                .save(comment)
                .doOnNext(comment1 -> {
                    // Send the update to the party
                    messagingTemplate.convertAndSend(
                            format("/party/%s",
                                    event.getPartyId()),
                            mapperFacade.map(comment1, CommentedResource.class)
                    );
                })
                .subscribe();
    }

    @Async
    @EventListener(LikeEvent.class)
    public void handleEvent(LikeEvent event) {
        cached
                .lookup(
                        "posts",
                        event.getId(),
                        Post.class,
                        () -> postRepository.findById(event.getId())
                )
                .flatMap(post -> {
                    final List<String> likes = Optional
                            .ofNullable(post.getLikes())
                            .orElseGet(ArrayList::new);
                    likes.add(event.getGuestId());
                    post.setLikes(likes
                            .stream()
                            .distinct()
                            .collect(Collectors.toList())
                    );
                    return postRepository.save(post);
                })
                .doOnNext(post -> {
                    // Send the update to the party
                    messagingTemplate.convertAndSend(format("/party/%s", event.getPartyId()), LikedResource
                            .builder()
                            .id(event.getId())
                            .postId(event.getPostId())
                            .likes(post.getLikes().size())
                            .build()
                    );
                })
                .subscribe();
    }

    @Async
    @EventListener(UnlikeEvent.class)
    public void handleEvent(UnlikeEvent event) {
        cached
                .lookup(
                        "posts",
                        event.getId(),
                        Post.class,
                        () -> postRepository.findById(event.getId())
                )
                .flatMap(post -> {
                    List<String> likes = Optional
                            .ofNullable(post.getLikes())
                            .orElseGet(ArrayList::new);
                    likes.remove(event.getGuestId());
                    post.setLikes(likes);
                    return postRepository.save(post);
                })
                .doOnNext(post -> {
                    // Send the update to the party
                    messagingTemplate.convertAndSend(format("/party/%s", event.getPartyId()), LikedResource
                            .builder()
                            .id(event.getId())
                            .postId(event.getPostId())
                            .likes(post.getLikes().size())
                            .build()
                    );
                })
                .subscribe();
    }

    @Async
    @EventListener(StartPartyEvent.class)
    public void handleEvent(StartPartyEvent event) {
        // Send the update to the party
        messagingTemplate.convertAndSend(format("/party/%s", event.getPartyId()), PartyStateResource
                .builder()
                .status(PartyStatus.IN_PROGRESS)
                .build()
        );
    }

    @Async
    @EventListener(EndPartyEvent.class)
    public void handleEvent(EndPartyEvent event) {
        // Send the update to the party
        messagingTemplate.convertAndSend(format("/party/%s", event.getPartyId()), PartyStateResource
                .builder()
                .status(PartyStatus.ENDED)
                .build()
        );
    }

    @Async
    @EventListener(AttendeeGuestUpdatedEvent.class)
    public void handleEvent(AttendeeGuestUpdatedEvent event) {
        // Send the update to the party
        messagingTemplate.convertAndSend(format("/party/%s", event.getPartyId()), AttendeeResource
                .builder()
                .id(event.getId())
                .guest(event.getGuest())
                .build()
        );
    }

    @Async
    @EventListener(JoinedPartyEvent.class)
    public void handleEvent(JoinedPartyEvent event) {
        Attendee attendee = event.getWho();
        attendee.setStatus(Attendee.AttendeeStatus.JOINED);
        attendeeRepository
                .save(attendee)
                .subscribe();
    }

    @Async
    @EventListener(LeftPartyEvent.class)
    public void handleEvent(LeftPartyEvent event) {
        Attendee attendee = event.getWho();
        attendee.setStatus(Attendee.AttendeeStatus.LEFT_EARLY);
        attendeeRepository
                .save(attendee)
                .subscribe();
    }

}
