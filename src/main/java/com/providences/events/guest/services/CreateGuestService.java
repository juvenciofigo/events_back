package com.providences.events.guest.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.providences.events.event.dto.CreataSeatDTO;
import com.providences.events.event.entities.EventEntity;
import com.providences.events.event.repositories.EventRepository;
import com.providences.events.guest.GuestEntity;
import com.providences.events.guest.GuestRepository;
import com.providences.events.guest.dto.CreateGuestDTO;
import com.providences.events.shared.exception.exceptions.ForbiddenException;
import com.providences.events.shared.exception.exceptions.ResourceNotFoundException;
import com.providences.events.ticket.dto.CreateTicketDTO;
import com.providences.events.ticket.entities.TicketEntity;
import com.providences.events.ticket.services.CreateTicketService;

@Service
@Transactional
public class CreateGuestService {
        private final CreateTicketService createTicketService;
        private final GuestRepository guestRepository;
        private final EventRepository eventRepository;

        public CreateGuestService(CreateTicketService createTicketService, GuestRepository guestRepository,
                        EventRepository eventRepository) {
                this.createTicketService = createTicketService;
                this.guestRepository = guestRepository;
                this.eventRepository = eventRepository;
        }

        public CreateGuestDTO.Response execute(CreateGuestDTO.Request data, String userId) {

                // buscar evento
                EventEntity event = eventRepository.getEventById(data.getEventId());

                if (event == null) {
                        throw new ResourceNotFoundException("Evento não encontrado!");
                }
                if (!event.getOrganizer().getUser().getId().equals(userId)) {
                        throw new ForbiddenException("Sem permissão!");
                }

                GuestEntity guest = new GuestEntity();
                guest.setName(data.getName());
                guest.setEmail(data.getEmail());
                guest.setPhone(data.getPhone());

                // Criar convite
                TicketEntity ticket = createTicketService.execute(event, data, guest);
                guest.setTicket(ticket);

                // Gravar informacoes do ticket
                GuestEntity savedGuest = guestRepository.save(guest);

                CreataSeatDTO.Response responseSeat = new CreataSeatDTO.Response(ticket.getSeat().getId(),
                                ticket.getSeat().getName(),
                                ticket.getSeat().getDescription(),
                                ticket.getSeat().getTotalSeats(),
                                ticket.getSeat().getAvailableSeats(),
                                ticket.getSeat().getLayoutPositionX(),
                                ticket.getSeat().getLayoutPositionY());

                CreateTicketDTO.Response responseTicket = new CreateTicketDTO.Response(ticket.getCode(),
                                ticket.getTotalPeople(),
                                ticket.getNotes(),
                                ticket.getStatus(),
                                ticket.getSentAt(),
                                ticket.getRespondedAt(),
                                responseSeat);

                CreateGuestDTO.Response responseGuest = new CreateGuestDTO.Response(
                                savedGuest.getId(),
                                savedGuest.getName(),
                                savedGuest.getEmail(),
                                savedGuest.getPhone(),
                                responseTicket);
                // ticket.getSeat(),

                return responseGuest;
        }
}