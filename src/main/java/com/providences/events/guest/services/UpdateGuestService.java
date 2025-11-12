package com.providences.events.guest.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.providences.events.event.entities.EventEntity;
import com.providences.events.guest.GuestEntity;
import com.providences.events.guest.GuestRepository;
import com.providences.events.guest.dto.GuestDTO;
import com.providences.events.shared.exception.exceptions.ForbiddenException;
import com.providences.events.shared.exception.exceptions.ResourceNotFoundException;
import com.providences.events.ticket.services.UpdateTicketService;

@Service
@Transactional
public class UpdateGuestService {
        private GuestRepository guestRepository;
        private UpdateTicketService updateTicketService;

        public UpdateGuestService(GuestRepository guestRepository,
                        UpdateTicketService updateTicketService) {
                this.guestRepository = guestRepository;
                this.updateTicketService = updateTicketService;
        }

        public GuestDTO.Response execute(GuestDTO.Update data, String userId) {

                GuestEntity guest = guestRepository.guestById(data.getGuestId())
                                .orElseThrow(() -> new ResourceNotFoundException("Convidado não encontrado!"));

                // buscar evento
                EventEntity event = guest.getTicket().getEvent();

                if (!event.getOrganizer().getUser().getId().equals(userId)) {
                        throw new ForbiddenException("Sem permissão!");

                }
                guest.setName(data.getName());
                guest.setEmail(data.getEmail());
                guest.setPhone(data.getPhone());

                // Criar convite
                updateTicketService.execute(guest.getTicket(), data);

                // Gravar informacoes do ticket
                GuestEntity savedGuest = guestRepository.save(guest);

                GuestDTO.Response responseGuest = GuestDTO.Response.response(savedGuest);

                return responseGuest;
        }
}