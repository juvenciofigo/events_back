package com.providences.events.organizer.dto;

import com.providences.events.organizer.OrganizerEntity;
import com.providences.events.user.dto.UserDTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
        @NotNull(message = "O campo nome é obrigatário!")
        @NotBlank(message = "Preencha o campo nome")
        @Size(max = 100, min = 3, message = "O nome deve ter de 3 à 100 caracteres!")
        private String name;

        private String phone;

    }

    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Response {
        private String id;
        private String name;
        private String profilePicture;
        private UserDTO.Response user;

        public static Response responseMe(OrganizerEntity organizer) {
            return new Response(organizer.getId(),
                    organizer.getName(),
                    organizer.getProfilePicture(),
                    organizer.getUser() != null ? UserDTO.Response.response(organizer.getUser(), null, null) : null);
        }

        public static Response response2(OrganizerEntity organizer) {

            Response res = new Response();
            res.setId(organizer.getId());
            res.setName(organizer.getName());
            res.setProfilePicture(organizer.getProfilePicture());
            return res;
        }
    }

}
