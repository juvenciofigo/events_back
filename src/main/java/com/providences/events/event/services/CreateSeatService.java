package com.providences.events.event.services;

import java.math.BigDecimal;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.providences.events.event.dto.CreateSeatDTO;
import com.providences.events.event.entities.EventEntity;
import com.providences.events.event.entities.SeatEntity;
import com.providences.events.event.repositories.EventRepository;
import com.providences.events.event.repositories.SeatRepository;
import com.providences.events.shared.exception.exceptions.BusinessException;
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

    public CreateSeatDTO.Response execute(CreateSeatDTO.Request data, String userId) {
        EventEntity event = eventRepository.findById(data.getEventId())
                .orElseThrow(() -> new ResourceNotFoundException("Evento não encontrado!"));

        if (!event.getOrganizer().getUser().getId().equals(userId)) {
            throw new ForbiddenException("Sem permissão!");
        }

        for (SeatEntity seat : event.getSeats()) {
            if (seat.getName().equals(data.getName())) {

                throw new BusinessException(
                        "Nome de assento existente! Crie com um nome diferente de " + data.getName(),
                        HttpStatus.CONFLICT);
            }
        }

        SeatEntity seat = new SeatEntity();

        if (Boolean.TRUE.equals(data.getIsPaid())
                && (data.getPrice() == null || data.getPrice().compareTo(BigDecimal.ZERO) <= 0)) {
            throw new BusinessException("O preço deve ser maior que zero para assentos pagos.", HttpStatus.BAD_REQUEST);
        }

        if (data.getTotalSeats() != null) {
            seat.setTotalSeats(data.getTotalSeats());
            seat.setAvailableSeats(data.getTotalSeats());
        }

        seat.setName(data.getName());
        seat.setDescription(data.getDescription());
        seat.setIsPaid(data.getIsPaid());
        seat.setPrice(data.getPrice());
        seat.setEvent(event);
        seat.setLayoutPositionY(data.getLayoutPositionY());
        seat.setLayoutPositionX(data.getLayoutPositionX());

        SeatEntity savedSeats = seatRepository.save(seat);

        return CreateSeatDTO.Response.response(savedSeats);

    }
}
