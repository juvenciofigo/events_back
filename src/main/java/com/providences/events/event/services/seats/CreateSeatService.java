package com.providences.events.event.services.seats;

import java.math.BigDecimal;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.providences.events.event.dto.SeatDTO;
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

    public SeatDTO.Response execute(String eventId, SeatDTO.Create data, String userId) {

        if (data.getIsPaid() == true && (data.getPrice() == null || data.getPrice().compareTo(BigDecimal.ZERO) <= 0)) {
            throw new BusinessException("Um assento pago deve ter o preço maior que zero.", HttpStatus.BAD_REQUEST);
        }

        EventEntity event = eventRepository.findId(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Evento não encontrado!"));

        if (!event.getOrganizer().getUser().getId().equals(userId)) {
            throw new ForbiddenException("Sem permissão!");
        }

        if (event.getSeats() != null) {
            for (SeatEntity seat : event.getSeats()) {
                if (seat.getName().trim().equalsIgnoreCase(data.getName().trim())) {
                    throw new BusinessException(
                            "Nome de assento existente! Crie com um nome diferente de " + data.getName(),
                            HttpStatus.CONFLICT);
                }
            }
        }

        SeatEntity seat = new SeatEntity();

        if (data.getTotalSeats() != null) {
            seat.setTotalSeats(data.getTotalSeats());
            seat.setAvailableSeats(data.getTotalSeats());
        }

        seat.setName(data.getName());
        seat.setDescription(data.getDescription() != null ? data.getDescription() : "");
        seat.setIsPaid(data.getIsPaid());
        seat.setPrice(data.getPrice() != null ? data.getPrice() : BigDecimal.ZERO);
        seat.setEvent(event);
        seat.setLayoutPositionY(data.getLayoutPositionY() != null ? data.getLayoutPositionY() : null);
        seat.setLayoutPositionX(data.getLayoutPositionX() != null ? data.getLayoutPositionX() : null);

        SeatEntity savedSeats = seatRepository.save(seat);

        return SeatDTO.Response.response(savedSeats);

    }
}
