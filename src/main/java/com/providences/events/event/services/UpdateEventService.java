package com.providences.events.event.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.providences.events.event.dto.EventDTO;
import com.providences.events.event.entities.EventEntity;
import com.providences.events.event.repositories.EventRepository;
import com.providences.events.shared.exception.exceptions.ForbiddenException;
import com.providences.events.shared.exception.exceptions.ResourceNotFoundException;

@Service
@Transactional
public class UpdateEventService {
    private EventRepository eventRepository;

    public UpdateEventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public EventDTO.Response execute(String eventId, EventDTO.Update data, String userId) {
        if (eventId.isBlank() || eventId == null) {
            throw new ResourceNotFoundException("Informe o eventId!");
        }

        EventEntity event = eventRepository.createGuest(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Evento não encontrado!"));

        if (!event.getOrganizer().getUser().getId().equals(userId)) {
            throw new ForbiddenException("Sem permissão!");
        }

        event.setType(data.getType() != null ? data.getType() : null);
        event.setIsPublic(data.getIsPublic() != null ? data.getIsPublic() : null);
        event.setTitle(data.getTitle() != null ? data.getTitle() : null);
        event.setDateStart(data.getDateStart() != null ? data.getDateStart() : null);
        event.setDateEnd(data.getDateEnd() != null ? data.getDateEnd() : null);
        event.setCoverImage(data.getCoverImage() != null ? data.getCoverImage() : null);
        event.setDescription(data.getDescription() != null ? data.getDescription() : null);
        event.setEstimatedGuest(data.getEstimatedGuest() != null ? data.getEstimatedGuest() : null);
        event.setBudgetEstimated(data.getBudgetEstimated() != null ? data.getBudgetEstimated() : null);
        event.setBudgetSpent(data.getBudgetSpent() != null ? data.getBudgetSpent() : null);

        return EventDTO.Response.response(eventRepository.save(event));

    }
}
