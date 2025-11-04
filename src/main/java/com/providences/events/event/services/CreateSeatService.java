package com.providences.events.event.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.providences.events.event.dto.CreataSeatsDTO;
import com.providences.events.event.entities.EventEntity;
import com.providences.events.event.entities.SeatsEntity;
import com.providences.events.event.repositories.EventRepository;
import com.providences.events.event.repositories.SeatRepository;
import com.providences.events.shared.exception.exceptions.ForbiddenException;
import com.providences.events.shared.exception.exceptions.ResourceNotFoundException;

@Service
@Transactional
public class CreateSeatService {
    private final SeatRepository seatRepository;

    private final EventRepository eventRepository;

    public CreateSeatService(SeatRepository seatRepository, EventRepository eventRepository) {
        this.seatRepository = seatRepository;
        this.eventRepository = eventRepository;
    }

    public CreataSeatsDTO.Response execute(CreataSeatsDTO.Request data, String userId) {
        EventEntity event = eventRepository.getEventById(data.getEventId());

        if (event == null) {

            throw new ResourceNotFoundException("Evento não encontrado!");
        }

        if (!event.getOrganizer().getUser().getId().equals(userId)) {
            throw new ForbiddenException("Sem permissão!");
        }

        SeatsEntity seat = new SeatsEntity();
        seat.setName(data.getName());
        seat.setDescription(data.getDescription());
        seat.setTotalSeats(data.getTotalSeats());
        seat.setEvent(event);
        seat.setLayoutPositionX(data.getLayoutPositionX());
        seat.setLayoutPositionY(data.getLayoutPositionY());
        SeatsEntity savedSeats = seatRepository.save(seat);

        return new CreataSeatsDTO.Response(
                savedSeats.getId(),
                savedSeats.getName(),
                savedSeats.getDescription(),
                savedSeats.getTotalSeats(),
                savedSeats.getLayoutPositionX(),
                savedSeats.getLayoutPositionY());

    }
}
