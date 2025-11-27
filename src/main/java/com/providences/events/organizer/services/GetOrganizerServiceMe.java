package com.providences.events.organizer.services;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.providences.events.organizer.OrganizerEntity;
import com.providences.events.organizer.OrganizerRepository;
import com.providences.events.organizer.dto.OrganizerDTO;
import com.providences.events.shared.exception.exceptions.BusinessException;


@Service
@Transactional(readOnly = true)
public class GetOrganizerServiceMe {
    private final OrganizerRepository organizerRepository;

    public GetOrganizerServiceMe(OrganizerRepository organizerRepository) {
        this.organizerRepository = organizerRepository;
    }

    public OrganizerDTO.Response execute(String organizerId, String userId) {
        OrganizerEntity organizer = organizerRepository.findId(organizerId)
                .orElseThrow(() -> new BusinessException("Organizer not found", HttpStatus.NOT_FOUND));

        if (organizer.getUser().getId().equals(userId)) {
            // If the requester is the owner, include user details
            return OrganizerDTO.Response.responseMe(organizer);
        }

       throw new BusinessException("Não autorizado", HttpStatus.FORBIDDEN);

    }
}
