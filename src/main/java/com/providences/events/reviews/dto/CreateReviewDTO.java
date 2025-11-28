package com.providences.events.reviews.dto;

import com.providences.events.reviews.ReviewEntity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


public class CreateReviewDTO {

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {

        @NotBlank(message = "Escreva seu comentário!")
        private String comment;

        @NotNull(message = "Dê sua avaliação")
        private Double rating;

        // Quem está enviando o review
        @NotBlank(message = "Informe quem envia o comentário!")
        private String sender;

        private String senderSupplierID;
        private String senderOrganizerID;

        // Para quem é o comentário
        @NotBlank(message = "Informe para quem é o comentário!")
        private String target;

        private String targetSupplierId;
        private String targetOrganizerID;
        private String targetServiceID;
    }


    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {

        private String id;
        private String comment;
        private Double rating;

        private String sender;
        // private String senderSupplierID;
        // private String senderOrganizerID;

        // Target
        private String target;
        // private String targetSupplierId;
        // private String targetOrganizerID;
        // private String targetServiceID;

        public static Response response(ReviewEntity review) {
            return new Response(
                    review.getId(),
                    review.getComment(),
                    review.getRating(),
                    review.getSender().name(),
                    review.getTarget().name());
        }
    }
}
