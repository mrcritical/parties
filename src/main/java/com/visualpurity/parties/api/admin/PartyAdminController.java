package com.visualpurity.parties.api.admin;

import com.visualpurity.parties.api.admin.model.NewPartyResource;
import com.visualpurity.parties.api.admin.model.UpdatePartyResource;
import com.visualpurity.parties.datastore.PartyRepository;
import com.visualpurity.parties.datastore.model.Creator;
import com.visualpurity.parties.datastore.model.Party;
import com.visualpurity.parties.listener.event.EndPartyEvent;
import com.visualpurity.parties.listener.event.StartPartyEvent;
import com.visualpurity.parties.listener.event.StartPartyEvent.PartyControlSource;
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
import java.time.LocalDateTime;

@RestController
@RequestMapping(value = "/api/admin/parties", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@RequiredArgsConstructor
public class PartyAdminController {

    @NonNull
    private final PartyRepository partyRepository;

    @NonNull
    private final MapperFacade mapperFacade;

    @NonNull
    private final ApplicationEventPublisher publisher;

    @GetMapping
    public Flux<Party> getParties() {
        return partyRepository.findAll();
    }

    @GetMapping("/{id}")
    public Mono<Party> getParty(@PathVariable String id) {
        return partyRepository.findById(id);
    }

    @PostMapping("/")
    public Mono<Party> createParty(@Valid @RequestBody NewPartyResource resource,
                                   @AuthenticationPrincipal TokenUserDetails principal) {
        Party party = mapperFacade.map(resource, Party.class);
        party.setAccountId(principal.getUser().getAccountId());
        party.setCreatedBy(
                Creator
                        .builder()
                        .at(LocalDateTime.now())
                        .user(principal.getUser())
                        .build()
        );
        party.setStatus(Party.PartyStatus.NOT_STARTED);
        return partyRepository.save(party);
    }

    @PutMapping("/{id}/start")
    public Mono<ResponseEntity<Void>> startParty(@PathVariable String id) {
        return partyRepository
                .findById(id)
                .flatMap(party -> {
                    party.setStatus(Party.PartyStatus.IN_PROGRESS);
                    return partyRepository.save(party);
                })
                .doOnNext(party -> publisher.publishEvent(
                        StartPartyEvent
                                .builder()
                                .partyId(party.getId())
                                .type(PartyControlSource.MANUAL)
                                .build()
                        )
                )
                .map(party -> ResponseEntity.accepted().build());
    }

    @PutMapping("/{id}/stop")
    public Mono<ResponseEntity<Void>> endParty(@PathVariable String id) {
        return partyRepository
                .findById(id)
                .flatMap(party -> {
                    party.setStatus(Party.PartyStatus.ENDED);
                    return partyRepository.save(party);
                })
                .doOnNext(party -> publisher.publishEvent(
                        EndPartyEvent
                                .builder()
                                .partyId(party.getId())
                                .type(PartyControlSource.MANUAL)
                                .build()
                        )
                )
                .map(party -> ResponseEntity.accepted().build());
    }

    @PutMapping("/{id}")
    public Mono<Party> updateParty(@PathVariable String id, @RequestBody UpdatePartyResource resource) {
        return partyRepository
                .findById(id)
                .flatMap(party -> {
                    mapperFacade.map(resource, party);
                    return partyRepository.save(party);
                });
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteParty(@PathVariable String id) {
        return partyRepository
                .deleteById(id)
                .map(aVoid -> ResponseEntity
                        .accepted()
                        .build()
                );
    }

}
