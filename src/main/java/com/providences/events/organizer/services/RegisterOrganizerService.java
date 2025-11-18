package com.providences.events.organizer.services;

import org.springframework.http.HttpStatus;

import com.providences.events.organizer.OrganizerEntity;
import com.providences.events.organizer.OrganizerRepository;
import com.providences.events.organizer.dto.OrganizerDTO;
import com.providences.events.shared.exception.exceptions.BusinessException;
import com.providences.events.user.UserEntity;
import com.providences.events.user.UserRepository;
import com.providences.events.user.dto.UserDTO;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class RegisterOrganizerService {

        private OrganizerRepository organizerRepository;
        private UserRepository userRepository;

        public RegisterOrganizerService(OrganizerRepository organizerRepository, UserRepository userRepository) {
                this.organizerRepository = organizerRepository;
                this.userRepository = userRepository;
        }

        public OrganizerDTO.Response execute(OrganizerDTO.Create data, String userId) {

                UserEntity user = userRepository.findId(userId)
                                .filter(u -> !u.getIsDeleted())
                                .orElseThrow(() -> new BusinessException("Utilizador não encontrado",
                                                HttpStatus.NOT_FOUND));

                boolean alreadyHasSupplier = organizerRepository.existsByUserId(userId);

                if (alreadyHasSupplier) {
                        throw new BusinessException("Esse utilizador já possui um organizador!", HttpStatus.CONFLICT);
                }

                OrganizerEntity organizer = new OrganizerEntity();
                organizer.setName(data.getName());
                organizer.setPhone(data.getPhone());
                organizer.setProfilePicture("");
                organizer.setUser(user);

                OrganizerEntity savedOrganizer = organizerRepository.save(organizer);

                UserDTO.Response userDTO = UserDTO.Response.response(savedOrganizer.getUser(), null, null);

                return OrganizerDTO.Response.response(savedOrganizer, userDTO);

        }
}
