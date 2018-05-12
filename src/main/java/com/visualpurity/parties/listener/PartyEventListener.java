package com.visualpurity.parties.listener;

import com.visualpurity.parties.api.model.guest.LikedResource;
import com.visualpurity.parties.datastore.AttendeeRepository;
import com.visualpurity.parties.datastore.PartyRepository;
import com.visualpurity.parties.datastore.model.Attendee;
import com.visualpurity.parties.datastore.model.Post;
import com.visualpurity.parties.listener.event.JoinedPartyEvent;
import com.visualpurity.parties.listener.event.LeftPartyEvent;
import com.visualpurity.parties.listener.event.LikeEvent;
import com.visualpurity.parties.listener.event.PartyPostEvent;
import com.visualpurity.parties.listener.event.PostCommentEvent;
import com.visualpurity.parties.listener.event.UnlikeEvent;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class PartyEventListener {

    @NonNull
    private final PartyRepository partyRepository;

    @NonNull
    private final AttendeeRepository attendeeRepository;

    @NonNull
    private final SimpMessageSendingOperations messagingTemplate;

    @Async
    @EventListener(PartyPostEvent.class)
    public void handleEvent(PartyPostEvent event) {
        Post post = Post
                .builder()
                .id(event.getId())
                .text(event.getText())
                .by(event.getBy().getGuest())
                .at(event.getAt())
                .media(new ArrayList<>())
                // TODO Add media
                .build();
        partyRepository
                .findById(event.getPartyId())
                .flatMap(party -> {
                    List<Post> posts = Optional
                            .ofNullable(party.getPosts())
                            .orElseGet(() -> {
                                List<Post> postList = new ArrayList<>();
                                party.setPosts(postList);
                                return postList;
                            });
                    posts.add(post);
                    return partyRepository.save(party);
                })
                .subscribe();
    }

    @Async
    @EventListener(PostCommentEvent.class)
    public void handleEvent(PostCommentEvent event) {
        Post comment = Post
                .builder()
                .id(event.getId())
                .text(event.getText())
                .by(event.getBy().getGuest())
                .at(event.getAt())
                .build();
        partyRepository
                .findById(event.getPartyId())
                .flatMap(party -> {
                    party.getPosts()
                            .stream()
                            .filter(post -> post.getId().equals(event.getPostId()))
                            .findFirst()
                            .ifPresent(post -> {
                                List<Post> comments = Optional
                                        .ofNullable(post.getComments())
                                        .orElseGet(() -> {
                                            List<Post> commentList = new ArrayList<>();
                                            post.setComments(commentList);
                                            return commentList;
                                        });
                                comments.add(comment);
                            });
                    return partyRepository.save(party);
                })
                .subscribe();
    }

    @Async
    @EventListener(LikeEvent.class)
    public void handleEvent(LikeEvent event) {
        partyRepository
                .findById(event.getPartyId())
                .flatMap(party -> {
                    party.getPosts()
                            .stream()
                            .filter(post -> post.getId().equals(event.getPostId()) || post.getId().equals(event.getId()))
                            .findFirst()
                            .ifPresent(post -> {
                                Post postToLike = Optional
                                        .ofNullable(event.getPostId())
                                        .flatMap(id1 -> post.getComments()
                                                .stream()
                                                .filter(comment -> comment.getId().equals(post.getId()))
                                                .findFirst()
                                        )
                                        .orElse(post);
                                final Integer likes = Optional
                                        .ofNullable(postToLike.getLikes())
                                        .orElse(0) + 1;
                                postToLike.setLikes(likes);

                                // Send the update to the party
                                messagingTemplate.convertAndSend(String.format("/party/%s", event.getPartyId()), LikedResource
                                        .builder()
                                        .id(event.getId())
                                        .postId(event.getPostId())
                                        .likes(postToLike.getLikes())
                                        .build()
                                );
                            });
                    return partyRepository.save(party);
                })
                .subscribe();
    }

    @Async
    @EventListener(UnlikeEvent.class)
    public void handleEvent(UnlikeEvent event) {
        partyRepository
                .findById(event.getPartyId())
                .flatMap(party -> {
                    party.getPosts()
                            .stream()
                            .filter(post -> post.getId().equals(event.getPostId()) || post.getId().equals(event.getId()))
                            .findFirst()
                            .ifPresent(post -> {
                                Post postToLike = Optional
                                        .ofNullable(event.getPostId())
                                        .flatMap(id1 -> post.getComments()
                                                .stream()
                                                .filter(comment -> comment.getId().equals(post.getId()))
                                                .findFirst()
                                        )
                                        .orElse(post);
                                final Integer likes = Optional
                                        .ofNullable(postToLike.getLikes())
                                        .orElse(0) - 1;
                                postToLike.setLikes(likes >= 0 ? likes : 0);

                                // Send the update to the party
                                messagingTemplate.convertAndSend(String.format("/party/%s", event.getPartyId()), LikedResource
                                        .builder()
                                        .id(event.getId())
                                        .postId(event.getPostId())
                                        .likes(postToLike.getLikes())
                                        .build()
                                );
                            });
                    return partyRepository.save(party);
                })
                .subscribe();
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
