package com.providences.events.guest.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.providences.events.event.entities.EventEntity;
import com.providences.events.event.repositories.EventRepository;
import com.providences.events.guest.GuestEntity;
import com.providences.events.guest.GuestRepository;
import com.providences.events.guest.dto.GuestDTO;
import com.providences.events.shared.dto.SystemDTO;
import com.providences.events.shared.exception.exceptions.ForbiddenException;
import com.providences.events.shared.exception.exceptions.ResourceNotFoundException;

@Service
@Transactional(readOnly = true)
public class FetchGuestsService {
    private final EventRepository eventRepository;
    private final GuestRepository guestRepository;

    public FetchGuestsService(EventRepository eventRepository, GuestRepository guestRepository) {
        this.eventRepository = eventRepository;
        this.guestRepository = guestRepository;
    }

    public SystemDTO.ItemWithPage<GuestDTO.Response> execute(String eventId, String userId, int pageNumber, int limit,
            String sort) {

        EventEntity event = eventRepository.findIdFetchTicketsAndGuests(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Evento não encontrado!"));

        if (!event.getOrganizer().getUser().getId().equals(userId)) {
            throw new ForbiddenException("Sem permissão!");
        }

        int validPage = Math.max(pageNumber - 1, 0);
        PageRequest pageRequest = PageRequest.of(validPage, limit, Sort.by(sort).descending());

        Page<GuestEntity> guests = guestRepository.fetchByEventId(eventId, pageRequest);

        List<GuestDTO.Response> list = guests.stream()
                .map(g -> GuestDTO.Response.response(g))
                .toList();

        return new SystemDTO.ItemWithPage<>(
                list,
                pageNumber,
                guests.getTotalPages(),
                guests.getTotalElements());
    }

}
