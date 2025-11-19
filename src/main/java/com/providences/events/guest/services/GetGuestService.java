package com.providences.events.guest.services;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.providences.events.event.entities.EventEntity;
import com.providences.events.guest.GuestEntity;
import com.providences.events.guest.GuestRepository;
import com.providences.events.guest.dto.GuestDTO;
import com.providences.events.shared.exception.exceptions.BusinessException;
import com.providences.events.shared.exception.exceptions.ForbiddenException;

@Service
@Transactional(readOnly = true)
public class GetGuestService {
    private GuestRepository guestRepository;

    public GetGuestService(GuestRepository guestRepository) {
        this.guestRepository = guestRepository;
    }

    public GuestDTO.Response execute(String guestId, String userId) {

        GuestEntity guest = guestRepository.guestById(guestId)
                .orElseThrow(() -> new BusinessException("Convidado não encontrado!", HttpStatus.NOT_FOUND));

        // buscar evento
        EventEntity event = guest.getTicket().getEvent();

        if (!event.getOrganizer().getUser().getId().equals(userId)) {
            throw new ForbiddenException("Sem permissão!");
        }

        return GuestDTO.Response.response(guest);
    }
}
