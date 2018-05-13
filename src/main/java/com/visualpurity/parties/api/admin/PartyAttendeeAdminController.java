package com.visualpurity.parties.api.admin;

import com.visualpurity.parties.api.admin.model.NewAttendeeResource;
import com.visualpurity.parties.api.admin.model.UpdateAttendeeResource;
import com.visualpurity.parties.api.model.AttendeeResource;
import com.visualpurity.parties.api.model.ProfileResource;
import com.visualpurity.parties.datastore.AttendeeRepository;
import com.visualpurity.parties.datastore.GuestRepository;
import com.visualpurity.parties.datastore.model.Attendee;
import com.visualpurity.parties.datastore.model.profile.Guest;
import com.visualpurity.parties.listener.event.AttendeeGuestUpdatedEvent;
import com.visualpurity.parties.security.token.TokenUserDetails;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import ma.glasnost.orika.MapperFacade;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/api/admin/parties/{partyId}/attendees", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@RequiredArgsConstructor
public class PartyAttendeeAdminController {

    @NonNull
    private final AttendeeRepository attendeeRepository;

    @NonNull
    private final GuestRepository guestRepository;

    @NonNull
    private final MapperFacade mapperFacade;

    @NonNull
    private final ApplicationEventPublisher publisher;

    @GetMapping
    public Flux<Attendee> getAttendees(@PathVariable String partyId) {
        return attendeeRepository.findAllByPartyId(partyId);
    }

    @GetMapping("/{id}")
    public Mono<Attendee> getAttendee(@PathVariable String partyId,
                                      @PathVariable String id) {
        return attendeeRepository.findById(id);
    }

    @PostMapping("/")
    public Mono<Attendee> addAttendee(@PathVariable String partyId,
                                      @Valid @RequestBody NewAttendeeResource resource,
                                      @AuthenticationPrincipal TokenUserDetails principal) {
        Guest guest = mapperFacade.map(resource.getGuest(), Guest.class);
        guest.setAccountId(principal.getUser().getAccountId());
        return guestRepository
                .save(guest)
                .flatMap(guest1 -> attendeeRepository.save(
                        Attendee
                                .builder()
                                .guest(guest1)
                                .partyId(partyId)
                                .status(Attendee.AttendeeStatus.INVITATION_NOT_SENT)
                                .build()
                        )
                );
    }

    @PutMapping("/{id}/status/{status}")
    public Mono<ResponseEntity<Void>> updateAttendeeStatus(@PathVariable String partyId,
                                                           @PathVariable String id,
                                                           @PathVariable AttendeeResource.AttendeeStatus status) {
        return attendeeRepository
                .findById(id)
                .flatMap(attendee -> {
                    attendee.setStatus(Attendee.AttendeeStatus.valueOf(status.name()));
                    return attendeeRepository.save(attendee);
                })
                .map(attendee -> ResponseEntity.accepted().build());
    }

    @PutMapping("/{id}")
    public Mono<Attendee> updateAttendee(@PathVariable String partyId,
                                         @PathVariable String id,
                                         @RequestBody UpdateAttendeeResource resource) {
        Mono<Attendee> cached = attendeeRepository
                .findById(id)
                .cache();
        return cached
                .flatMap(attendee -> guestRepository
                        .findById(attendee.getGuest().getId())
                )
                .flatMap(guest -> {
                    mapperFacade.map(resource.getGuest(), guest);
                    return guestRepository
                            .save(guest);
                })
                .flatMap(guest -> cached
                        .map(attendee -> {
                            // Update without hitting the DB again, since this is already updated anyway
                            attendee.setGuest(guest);
                            return attendee;
                        })
                        .doOnNext(attendee -> publisher.publishEvent(
                                AttendeeGuestUpdatedEvent
                                        .builder()
                                        .id(id)
                                        .partyId(partyId)
                                        .guest(mapperFacade.map(attendee.getGuest(), ProfileResource.class))
                                        .build()
                                )
                        )
                );
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteAttendee(@PathVariable String partyId,
                                                     @PathVariable String id) {
        return attendeeRepository
                .deleteById(id)
                .map(aVoid -> ResponseEntity
                        .accepted()
                        .build()
                );
    }

}
