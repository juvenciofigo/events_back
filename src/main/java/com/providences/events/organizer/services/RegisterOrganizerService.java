package com.providences.events.organizer.services;

import org.springframework.beans.factory.annotation.Autowired;
import com.providences.events.organizer.OrganizerEntity;
import com.providences.events.organizer.OrganizerRepository;
import com.providences.events.organizer.dto.RegisterOrganizerDTO;
import com.providences.events.shared.exception.exceptions.ResourceNotFoundException;
import com.providences.events.user.UserEntity;
import com.providences.events.user.UserRepository;
import com.providences.events.user.dto.AuthUserDTO;

import org.springframework.stereotype.Service;

@Service
public class RegisterOrganizerService {

        @Autowired
        private OrganizerRepository organizerRepository;

        @Autowired
        private UserRepository userRepository;

        public RegisterOrganizerDTO.Response execute(RegisterOrganizerDTO.Request data, String userId) {

                UserEntity user = userRepository.findById(userId)
                                .orElseThrow(() -> new ResourceNotFoundException("Recurso não encontrado"));

                OrganizerEntity organizer = new OrganizerEntity();
                organizer.setName(data.getName());
                organizer.setPhone(data.getPhone());
                organizer.setProfilePicture(data.getProfilePicture());
                organizer.setUser(user);

                OrganizerEntity savedOrganizer = organizerRepository.save(organizer);

                AuthUserDTO.Response userDTO = AuthUserDTO.Response.response(savedOrganizer.getUser(), null);

                return RegisterOrganizerDTO.Response.response(savedOrganizer, userDTO);

        }
}
