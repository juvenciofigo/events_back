package com.providences.events.event.services.seats;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.providences.events.event.dto.SeatDTO;
import com.providences.events.event.entities.EventEntity;
import com.providences.events.event.entities.SeatEntity;
import com.providences.events.event.repositories.EventRepository;
import com.providences.events.event.repositories.SeatRepository;
import com.providences.events.shared.dto.SystemDTO;
import com.providences.events.shared.exception.exceptions.ForbiddenException;
import com.providences.events.shared.exception.exceptions.ResourceNotFoundException;

@Service
@Transactional
public class FetchSeatsService {
    private final EventRepository eventRepository;
    private final SeatRepository seatRepository;

    public FetchSeatsService(EventRepository eventRepository, SeatRepository seatRepository) {
        this.eventRepository = eventRepository;
        this.seatRepository = seatRepository;
    }

    public SystemDTO.ItemWithPage<SeatDTO.Response> execute(String eventId, String userId, int pageNumber, int limit,
            String sort) {
        EventEntity event = eventRepository.createGuest(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Evento não encontrado!"));

        if (!event.getOrganizer().getUser().getId().equals(userId)) {
            throw new ForbiddenException("Sem permissão!");
        }

        int validPage = Math.max(pageNumber - 1, 0);
        PageRequest pageRequest = PageRequest.of(validPage, limit, Sort.by(sort).descending());

        Page<SeatEntity> seats = seatRepository.fetchByEventId(eventId, pageRequest);

        List<SeatDTO.Response> list = seats.stream()
                .map(SeatDTO.Response::response)
                .toList();

        List<SeatDTO.Response> list2 = seatRepository.fetchByEvent(eventId).stream()
                .map(SeatDTO.Response::response)
                .toList();

                System.out.println(list2);
        System.out.println(list);
        return new SystemDTO.ItemWithPage<>(
                list,
                pageNumber,
                seats.getTotalPages(),
                seats.getTotalElements());
    }
}
