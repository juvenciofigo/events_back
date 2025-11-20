package com.providences.events.ticket.services;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.providences.events.event.entities.EventEntity;
import com.providences.events.event.repositories.EventRepository;
import com.providences.events.shared.exception.exceptions.BusinessException;
import com.providences.events.ticket.dto.TicketDTO;
import com.providences.events.ticket.entities.TicketEntity;

@Service
@Transactional(readOnly = true)
public class FetchEventTicketsService {
    private EventRepository eventRepository;

    public FetchEventTicketsService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public Set<TicketDTO.Response> execute(String eventId, String userId) {
        EventEntity event = eventRepository.findId(eventId)
                .orElseThrow(() -> new BusinessException("Evento não encontrado", HttpStatus.NOT_FOUND));

        if (!event.getOrganizer().getUser().getId().equals(userId)) {
            throw new BusinessException("Sem autorização ", HttpStatus.FORBIDDEN);
        }

        Set<TicketEntity> tickets = new HashSet<>(event.getTickets());

        return tickets.stream().map(TicketDTO.Response::response).collect(Collectors.toSet());

    }

}
