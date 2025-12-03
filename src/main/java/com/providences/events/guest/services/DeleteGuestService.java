package com.providences.events.guest.services;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.providences.events.event.entities.EventEntity;
import com.providences.events.event.entities.SeatEntity;
import com.providences.events.guest.GuestEntity;
import com.providences.events.guest.GuestRepository;
import com.providences.events.guest.dto.GuestDTO;
import com.providences.events.shared.dto.SystemDTO;
import com.providences.events.shared.exception.exceptions.BusinessException;
import com.providences.events.shared.exception.exceptions.ForbiddenException;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Service
@Transactional
public class DeleteGuestService {
    private final GuestRepository guestRepository;
    private final FetchGuestsService fetchGuestsService;
    @PersistenceContext
    private EntityManager em;

    public DeleteGuestService(GuestRepository guestRepository, FetchGuestsService fetchGuestsService) {
        this.guestRepository = guestRepository;
        this.fetchGuestsService = fetchGuestsService;
    }

    public SystemDTO.ItemWithPage<GuestDTO.Response> execute(String guestId, String userId) {

        GuestEntity guest = guestRepository.guestById(guestId)
                .orElseThrow(() -> new BusinessException("Guest não encontrado", HttpStatus.NOT_FOUND));

        EventEntity event = guest.getTicket().getEvent();

        if (!event.getOrganizer().getUser().getId().equals(userId)) {
            throw new ForbiddenException("Sem permissão!");
        }

        SeatEntity seat = guest.getTicket().getSeat();
        seat.setAvailableSeats(seat.getAvailableSeats() + guest.getTicket().getTotalPeople());

        guestRepository.delete(guest);

        em.flush();
        em.clear();

        int pageNumber = 1;
        int limit = 10;
        String sort = "createdAt";
        return fetchGuestsService.execute(event.getId(), userId, pageNumber, limit, sort);
    }

}