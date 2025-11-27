package com.providences.events.organizer.services;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.providences.events.organizer.OrganizerEntity;
import com.providences.events.organizer.OrganizerRepository;
import com.providences.events.organizer.dto.OrganizerDTO;
import com.providences.events.shared.exception.exceptions.BusinessException;

@Service
@Transactional
public class UpdateOrganizerService {
    private final OrganizerRepository organizerRepository;

    public UpdateOrganizerService(OrganizerRepository organizerRepository) {
        this.organizerRepository = organizerRepository;
    }

    public OrganizerDTO.Response execute(String organizerId, OrganizerDTO.Create data, String userId) {
        OrganizerEntity organizer = organizerRepository.findId(organizerId)
                .orElseThrow(() -> new BusinessException("Organizador não encontrado", HttpStatus.NOT_FOUND));

        if (!organizer.getUser().getId().equals(userId)) {
            throw new BusinessException("Não autoridado", HttpStatus.FORBIDDEN);
        }

        organizer.setCompanyName(data.getCompanyName());
        organizer.setPhone(data.getPhone());
        organizer.setDescription(data.getDescription() != null ? data.getDescription() : "");
        organizerRepository.save(organizer);

        return null;
    }
}
