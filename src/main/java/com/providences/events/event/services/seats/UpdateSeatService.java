package com.providences.events.event.services.seats;

import java.math.BigDecimal;

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
public class UpdateSeatService {
    private final SeatRepository seatRepository;

    public UpdateSeatService(SeatRepository seatRepository) {
        this.seatRepository = seatRepository;
    }

    public SeatDTO.Response execute(String seatId, SeatDTO.Create data, String userId) {

        SeatEntity seat = seatRepository.getSeat(seatId)
                .orElseThrow(() -> new BusinessException("Assento não encontrado!", HttpStatus.BAD_REQUEST));

        EventEntity event = seat.getEvent();

        if (!event.getOrganizer().getUser().getId().equals(userId)) {
        throw new ForbiddenException("Sem permissão!");
        }

        if (!seat.getName().trim().equalsIgnoreCase(data.getName().trim())) {
            boolean nameExists = event.getSeats().stream()
                    .anyMatch(s -> s.getName().trim().equalsIgnoreCase(data.getName().trim()));

            if (nameExists) {
                throw new BusinessException(
                        String.format("Nome de assento existente! Crie com um nome diferente de %s", data.getName()),
                        HttpStatus.CONFLICT);
            }
        }

        if (Boolean.TRUE.equals(data.getIsPaid())
                && (data.getPrice() == null || data.getPrice().compareTo(BigDecimal.ZERO) <= 0)) {
            throw new BusinessException("O preço deve ser maior que zero para assentos pagos.", HttpStatus.BAD_REQUEST);
        }

        Integer oldTotal = seat.getTotalSeats();

        if (data.getTotalSeats() != null && oldTotal != null) {
            int available = seat.getAvailableSeats();
            int used = oldTotal - available;
            int newTotal = data.getTotalSeats();

            if (newTotal < oldTotal && used > newTotal) {
                throw new BusinessException(
                        String.format(
                                "Não é possível reduzir o número de lugares para %d, pois já existem %d lugares ocupados.",
                                newTotal,
                                used),
                        HttpStatus.CONFLICT);
            }

            if (newTotal != oldTotal) {
                seat.setTotalSeats(newTotal);
                seat.setAvailableSeats(available + newTotal - oldTotal);
            }

        } else if (data.getTotalSeats() != null) {

            seat.setTotalSeats(data.getTotalSeats());
            seat.setAvailableSeats(data.getTotalSeats());
        }

        seat.setName(data.getName());
        seat.setDescription(data.getDescription());
        seat.setIsPaid(data.getIsPaid());
        seat.setPrice(data.getPrice());
        seat.setLayoutPositionY(data.getLayoutPositionY());
        seat.setLayoutPositionX(data.getLayoutPositionX());

        SeatEntity savedSeats = seatRepository.save(seat);

        return SeatDTO.Response.response(savedSeats);

    }
}
