package com.visualpurity.parties.api;

import com.visualpurity.parties.api.model.AttendeeResource;
import com.visualpurity.parties.api.model.CommentResource;
import com.visualpurity.parties.api.model.LikeResource;
import com.visualpurity.parties.api.model.LikesResource;
import com.visualpurity.parties.api.model.PostResource;
import com.visualpurity.parties.api.model.ProfileResource;
import com.visualpurity.parties.cache.Cached;
import com.visualpurity.parties.datastore.AttendeeRepository;
import com.visualpurity.parties.datastore.GuestRepository;
import com.visualpurity.parties.datastore.PostRepository;
import com.visualpurity.parties.datastore.model.Attendee;
import com.visualpurity.parties.datastore.model.Post;
import com.visualpurity.parties.datastore.model.profile.Guest;
import com.visualpurity.parties.listener.event.JoinedPartyEvent;
import com.visualpurity.parties.listener.event.LeftPartyEvent;
import com.visualpurity.parties.listener.event.LikeEvent;
import com.visualpurity.parties.listener.event.PartyPostEvent;
import com.visualpurity.parties.listener.event.PostCommentEvent;
import com.visualpurity.parties.listener.event.UnlikeEvent;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import ma.glasnost.orika.MapperFacade;
import org.bson.types.ObjectId;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class PartyController {

    private static final String ATTENDEE_KEY = "attendee";

    @NonNull
    private final AttendeeRepository attendeeRepository;

    @NonNull
    private final PostRepository postRepository;

    @NonNull
    private final GuestRepository guestRepository;

    @NonNull
    private final ApplicationEventPublisher publisher;

    @NonNull
    private final MapperFacade mapperFacade;

    @NonNull
    private final Cached cached;

    @MessageMapping("/party/{id}/join")
    @SendTo("/party/{id}")
    public Mono<AttendeeResource> join(@DestinationVariable String id,
                                       @Payload String attendeeId,
                                       SimpMessageHeaderAccessor headerAccessor) {
        return cached
                .lookup(
                        "attendees",
                        attendeeId,
                        Attendee.class,
                        () -> attendeeRepository.findById(attendeeId)
                )
                .doOnNext(attendee -> {
                    headerAccessor.getSessionAttributes().put(ATTENDEE_KEY, attendee);
                    JoinedPartyEvent event = JoinedPartyEvent
                            .builder()
                            .at(LocalDateTime.now())
                            .partyId(id)
                            .who(attendee)
                            .build();
                    publisher.publishEvent(event);
                })
                .map(attendee -> to(attendee, AttendeeResource.AttendeeStatus.JOINED));
    }

    @MessageMapping("/party/{id}/leave")
    @SendTo("/party/{id}")
    public AttendeeResource leave(@DestinationVariable String id,
                                  SimpMessageHeaderAccessor headerAccessor) {
        Attendee attendee = (Attendee) headerAccessor.getSessionAttributes().get(ATTENDEE_KEY);

        // Send the event to any listeners
        LeftPartyEvent event = LeftPartyEvent
                .builder()
                .at(LocalDateTime.now())
                .partyId(id)
                .who(attendee)
                .build();
        publisher.publishEvent(event);

        return to(attendee, AttendeeResource.AttendeeStatus.LEFT_EARLY);
    }

    @MessageMapping("/party/{id}/post")
    public void post(@DestinationVariable String id,
                     @Payload PostResource post,
                     SimpMessageHeaderAccessor headerAccessor) {

        Attendee attendee = (Attendee) headerAccessor.getSessionAttributes().get(ATTENDEE_KEY);
        PartyPostEvent postData = PartyPostEvent
                .builder()
                .id(ObjectId.get().toString())
                .at(LocalDateTime.now())
                .by(attendee)
                .text(post.getText())
                .partyId(id)
                .build();
        Optional
                .ofNullable(post.getMedia())
                .map(media -> media
                        .stream()
                        .map(mediumResource -> mapperFacade.map(mediumResource, PartyPostEvent.PostedMedium.class))
                        .collect(Collectors.toList())
                )
                .ifPresent(postData::setMedia);
        publisher.publishEvent(postData);
    }

    @MessageMapping("/party/{id}/comment")
    public void comment(@DestinationVariable String id,
                        @Payload CommentResource comment,
                        SimpMessageHeaderAccessor headerAccessor) {
        Attendee attendee = (Attendee) headerAccessor.getSessionAttributes().get(ATTENDEE_KEY);
        PostCommentEvent commentEvent = PostCommentEvent
                .builder()
                .id(ObjectId.get().toString())
                .at(LocalDateTime.now())
                .by(attendee)
                .text(comment.getText())
                .postId(comment.getPostId())
                .partyId(id)
                .build();
        publisher.publishEvent(commentEvent);
    }

    // TODO Modify post/comment
    // TODO Remove post/comment

    @MessageMapping("/party/{id}/like")
    public void like(@DestinationVariable String id,
                     @Payload LikeResource like,
                     SimpMessageHeaderAccessor headerAccessor) {
        Attendee attendee = (Attendee) headerAccessor.getSessionAttributes().get(ATTENDEE_KEY);
        LikeEvent likeEvent = LikeEvent
                .builder()
                .id(like.getId())
                .postId(like.getPostId())
                .partyId(id)
                .guestId(attendee.getGuest().getId())
                .build();
        publisher.publishEvent(likeEvent);
    }

    @MessageMapping("/party/{id}/likes")
    @SendToUser
    public Mono<LikesResource> likes(@DestinationVariable String id,
                                     @Payload String postId) {
        return cached
                .lookup(
                        "posts",
                        postId,
                        Post.class,
                        () -> postRepository
                                .findById(postId)
                )
                .flatMap(post -> Optional
                        .ofNullable(post.getLikes())
                        .map(likes -> {
                            List<Mono<Guest>> guests = likes
                                    .stream()
                                    .map(guestId -> cached
                                            .lookup(
                                                    "guests",
                                                    guestId,
                                                    Guest.class,
                                                    () -> guestRepository.findById(guestId)
                                            )
                                    )
                                    .collect(Collectors.toList());
                            return Flux
                                    .merge(guests)
                                    .collect(Collectors.toList());
                        })
                        .orElseGet(Mono::empty))
                .map(guests -> LikesResource
                        .builder()
                        .count(guests.size())
                        .postId(postId)
                        .likes(mapperFacade.mapAsList(guests, ProfileResource.class))
                        .build()
                );
    }

    @MessageMapping("/party/{id}/unlike")
    public void unlike(@DestinationVariable String id,
                       @Payload LikeResource unlike,
                       SimpMessageHeaderAccessor headerAccessor) {
        Attendee attendee = (Attendee) headerAccessor.getSessionAttributes().get(ATTENDEE_KEY);
        UnlikeEvent likeEvent = UnlikeEvent
                .builder()
                .id(unlike.getId())
                .postId(unlike.getPostId())
                .partyId(id)
                .guestId(attendee.getGuest().getId())
                .build();
        publisher.publishEvent(likeEvent);
    }

    private AttendeeResource to(Attendee attendee, AttendeeResource.AttendeeStatus status) {
        AttendeeResource attendeeResource = mapperFacade.map(attendee, AttendeeResource.class);
        attendeeResource.setStatus(status);
        return attendeeResource;
    }

}
