package com.visualpurity.parties.api;

import com.visualpurity.parties.api.model.guest.AttendeeResource;
import com.visualpurity.parties.api.model.guest.CommentResource;
import com.visualpurity.parties.api.model.guest.CommentedResource;
import com.visualpurity.parties.api.model.guest.LikeResource;
import com.visualpurity.parties.api.model.guest.NameResource;
import com.visualpurity.parties.api.model.guest.PostResource;
import com.visualpurity.parties.api.model.guest.PostedResource;
import com.visualpurity.parties.api.model.guest.ProfileResource;
import com.visualpurity.parties.api.model.guest.media.PictureResource;
import com.visualpurity.parties.datastore.AttendeeRepository;
import com.visualpurity.parties.datastore.PartyRepository;
import com.visualpurity.parties.datastore.model.Attendee;
import com.visualpurity.parties.listener.event.JoinedPartyEvent;
import com.visualpurity.parties.listener.event.LeftPartyEvent;
import com.visualpurity.parties.listener.event.LikeEvent;
import com.visualpurity.parties.listener.event.PartyPostEvent;
import com.visualpurity.parties.listener.event.PostCommentEvent;
import com.visualpurity.parties.listener.event.UnlikeEvent;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Controller
@RequiredArgsConstructor
public class PartyController {

    private static final String ATTENDEE_KEY = "attendee";

    @NonNull
    private final PartyRepository partyRepository;

    @NonNull
    private final AttendeeRepository attendeeRepository;

    @NonNull
    private final ApplicationEventPublisher publisher;

    @MessageMapping("/party.join")
    @SendTo("/party/{id}")
    public Mono<AttendeeResource> join(@DestinationVariable String id,
                                       @Payload String attendeeId,
                                       SimpMessageHeaderAccessor headerAccessor) {
        return attendeeRepository
                .findById(attendeeId)
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

    @MessageMapping("/party.leave")
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

    @MessageMapping("/party.post")
    @SendTo("/party/{id}")
    public Mono<PostedResource> post(@DestinationVariable String id,
                                     @Payload PostResource post,
                                     SimpMessageHeaderAccessor headerAccessor) {

        Attendee attendee = (Attendee) headerAccessor.getSessionAttributes().get(ATTENDEE_KEY);
        return partyRepository
                .findById(id)
                .map(party -> {
                    PartyPostEvent postData = PartyPostEvent
                            .builder()
                            .id(RandomStringUtils.randomAlphanumeric(10))
                            .at(LocalDateTime.now())
                            .by(attendee)
                            .text(post.getText())
                            .partyId(id)
                            // TODO Handle media
                            .build();
                    publisher.publishEvent(postData);

                    return PostedResource
                            .builder()
                            .id(postData.getId())
                            .at(postData.getAt())
                            .by(to(attendee))
                            .likes(0)
                            .text(postData.getText())
                            // TODO Handle media
                            .build();
                });
    }

    @MessageMapping("/party.comment")
    @SendTo("/party/{id}")
    public Mono<CommentedResource> comment(@DestinationVariable String id,
                                           @Payload CommentResource comment,
                                           SimpMessageHeaderAccessor headerAccessor) {

        Attendee attendee = (Attendee) headerAccessor.getSessionAttributes().get(ATTENDEE_KEY);
        return partyRepository
                .findById(id)
                .map(party -> {
                    PostCommentEvent commentEvent = PostCommentEvent
                            .builder()
                            .id(RandomStringUtils.randomAlphanumeric(10))
                            .at(LocalDateTime.now())
                            .by(attendee)
                            .text(comment.getText())
                            .postId(comment.getPostId())
                            .partyId(id)
                            .build();
                    publisher.publishEvent(commentEvent);

                    return CommentedResource
                            .builder()
                            .id(commentEvent.getId())
                            .at(commentEvent.getAt())
                            .by(to(attendee))
                            .likes(0)
                            .postId(comment.getPostId())
                            .text(commentEvent.getText())
                            .build();
                });
    }

    // TODO Modify post/comment
    // TODO Remove post/comment

    @MessageMapping("/party.like")
    @SendTo("/party/{id}")
    public void like(@DestinationVariable String id,
                     @Payload LikeResource like) {
        partyRepository
                .findById(id)
                .doOnNext(party -> {
                    LikeEvent likeEvent = LikeEvent
                            .builder()
                            .id(like.getId())
                            .postId(like.getPostId())
                            .partyId(id)
                            .build();
                    publisher.publishEvent(likeEvent);
                })
                .subscribe();
    }

    @MessageMapping("/party.unlike")
    @SendTo("/party/{id}")
    public void unlike(@DestinationVariable String id,
                       @Payload LikeResource unlike) {
        partyRepository
                .findById(id)
                .doOnNext(party -> {
                    UnlikeEvent likeEvent = UnlikeEvent
                            .builder()
                            .id(unlike.getId())
                            .postId(unlike.getPostId())
                            .partyId(id)
                            .build();
                    publisher.publishEvent(likeEvent);
                })
                .subscribe();
    }

    private ProfileResource to(Attendee attendee) {
        return ProfileResource
                .builder()
                .name(
                        NameResource
                                .builder()
                                .first(attendee.getGuest().getName().getFirst())
                                .last(attendee.getGuest().getName().getLast())
                                .build()
                )
                .avatar(
                        PictureResource
                                .builder()
                                .url(attendee.getGuest().getAvatar().getUrl())
                                .build()
                )
                .build();
    }

    private AttendeeResource to(Attendee attendee, AttendeeResource.AttendeeStatus status) {
        return AttendeeResource
                .builder()
                .id(attendee.getId())
                .guest(to(attendee))
                .status(status)
                .build();
    }

}
