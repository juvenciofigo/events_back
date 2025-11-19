package com.providences.events.event.services.seats;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.providences.events.event.dto.SeatDTO;
import com.providences.events.event.entities.EventEntity;
import com.providences.events.event.entities.SeatEntity;
import com.providences.events.event.repositories.SeatRepository;
import com.providences.events.shared.exception.exceptions.BusinessException;
import com.providences.events.shared.exception.exceptions.ForbiddenException;

@Service
@Transactional
public class GetSeatService {
    private final SeatRepository seatRepository;

    public GetSeatService(SeatRepository seatRepository) {
        this.seatRepository = seatRepository;
    }

    public SeatDTO.Response execute(String seatId, String userId) {

        SeatEntity seat = seatRepository.getSeat(seatId)
                .orElseThrow(() -> new BusinessException("Assento não encontrado!", HttpStatus.BAD_REQUEST));

        EventEntity event = seat.getEvent();

        if (!event.getOrganizer().getUser().getId().equals(userId)) {
            throw new ForbiddenException("Sem permissão!");
        }

        return SeatDTO.Response.response(seat);

    }
}
