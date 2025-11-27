package com.providences.events.organizer.dto;

import com.providences.events.organizer.OrganizerEntity;
import com.providences.events.user.dto.UserDTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class OrganizerDTO {

    @Setter
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Create {
        @NotBlank(message = "Preencha o campo nome")
        @Size(max = 100, min = 3, message = "O nome deve ter de 3 à 100 caracteres!")
        private String companyName;

        private String phone;

        private String description;
    }

    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Response {
        private String id;
        private String companyName;
        private String profilePicture;
        private String description;
        private UserDTO.Response user;

        public static Response responseMe(OrganizerEntity organizer) {
            return new Response(organizer.getId(),
                    organizer.getCompanyName(),
                    organizer.getProfilePicture(),
                    organizer.getDescription(),
                    organizer.getUser() != null ? UserDTO.Response.response(organizer.getUser(), null) : null);
        }

        public static Response response(OrganizerEntity organizer) {

            Response res = new Response();
            res.setId(organizer.getId());
            res.setCompanyName(organizer.getCompanyName());
            res.setProfilePicture(organizer.getProfilePicture());
            res.setDescription(organizer.getDescription());
            return res;
        }
    }

}
