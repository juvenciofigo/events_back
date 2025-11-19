package com.providences.events.reviews.services;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.providences.events.organizer.OrganizerEntity;
import com.providences.events.organizer.OrganizerRepository;
import com.providences.events.reviews.ReviewEntity;
import com.providences.events.reviews.ReviewsRepository;
import com.providences.events.reviews.ReviewEntity.ReviewSender;
import com.providences.events.reviews.ReviewEntity.ReviewTarget;
import com.providences.events.reviews.dto.CreateReviewDTO;
import com.providences.events.services.ServiceEntity;
import com.providences.events.services.ServiceRepository;
import com.providences.events.shared.exception.exceptions.BusinessException;
import com.providences.events.shared.exception.exceptions.ForbiddenException;
import com.providences.events.shared.exception.exceptions.ResourceNotFoundException;
import com.providences.events.supplier.SupplierEntity;
import com.providences.events.supplier.SupplierRepository;

@Service
@Transactional
public class CreateReviewService {
    private final ReviewsRepository reviewsRepository;
    private final OrganizerRepository organizerRepository;
    private final SupplierRepository supplierRepository;
    private final ServiceRepository serviceRepository;

    public CreateReviewService(ReviewsRepository reviewsRepository, OrganizerRepository organizerRepository,
            SupplierRepository supplierRepository, ServiceRepository serviceRepository) {
        this.reviewsRepository = reviewsRepository;
        this.organizerRepository = organizerRepository;
        this.supplierRepository = supplierRepository;
        this.serviceRepository = serviceRepository;
    }

    public CreateReviewDTO.Response execute(CreateReviewDTO.Request data, String userId) {
        ReviewEntity review = new ReviewEntity();
        review.setComment(data.getComment());
        review.setRating(data.getRating());

        if (!StringUtils.hasText(data.getReceiverOrganizerID()) && !StringUtils.hasText(data.getReceiverServiceID())
                && !StringUtils.hasText(data.getReceiverSupplierId())) {
            throw new BusinessException("Receiver desconhecido", HttpStatus.BAD_REQUEST);
        }

        if (!StringUtils.hasText(data.getSenderOrganizerID()) && !StringUtils.hasText(data.getSenderSupplierID())) {
            throw new BusinessException("Sender desconhecido", HttpStatus.BAD_REQUEST);
        }

        switch (data.getSender().toUpperCase()) {
            case "ORGANIZER":
                OrganizerEntity organizer = organizerRepository.findById(data.getSenderOrganizerID())
                        .orElseThrow(() -> new BusinessException("Sender Organizer não encontrado!", HttpStatus.BAD_REQUEST));

                review.setSenderOrganizer(organizer);
                review.setSender(ReviewSender.ORGANIZER);
                break;

            case "SUPPLIER":
                SupplierEntity supplier = supplierRepository.findById(data.getSenderSupplierID())
                        .orElseThrow(() -> new BusinessException("Sender Supplier Organizer não encontrado!", HttpStatus.BAD_REQUEST));

                review.setSenderSupplier(supplier);
                review.setSender(ReviewSender.SUPPLIER);
                break;

            default:
                throw new IllegalArgumentException("Sender desconhecido: " + data.getSender());
        }

        ReviewTarget target = switch (data.getTarget().toUpperCase()) {
            case "SERVICE" -> {
                ServiceEntity service = serviceRepository.getId(data.getReceiverServiceID())
                        .orElseThrow(() -> new ResourceNotFoundException("Serviço não encontrado!"));

                if (service.getSupplier().getUser().getId().equals(userId)) {
                    throw new ForbiddenException("Não tem permissao para fazer comentário para seu perfil!");
                }

                review.setReceiverService(service);
                yield ReviewTarget.SERVICE;
            }

            case "ORGANIZER" -> {
                OrganizerEntity organizer = organizerRepository.findById(data.getSenderOrganizerID())
                        .orElseThrow(() -> new ResourceNotFoundException("Organizador não encontrado!"));
                if (organizer.getUser().getId().equals(userId)) {
                    throw new ForbiddenException("Não tem permissao para fazer comentário para seu perfil!");
                }
                review.setReceiverOrganizer(organizer);
                yield ReviewTarget.ORGANIZER;
            }

            case "SUPPLIER" -> {
                SupplierEntity supplier = supplierRepository.findById(data.getSenderSupplierID())
                        .orElseThrow(() -> new ResourceNotFoundException("Fornecedor de serviços não encontrado!"));
                if (supplier.getUser().getId().equals(userId)) {
                    throw new ForbiddenException("Não tem permissao para fazer comentário para seu perfil!");
                }
                review.setReceiverSupplier(supplier);
                yield ReviewTarget.SUPPLIER;
            }

            default -> throw new BusinessException("Destinatário do comentário nao encontrado!", HttpStatus.BAD_REQUEST);
        };

        review.setTarget(target);

        ReviewEntity savedReview = reviewsRepository.save(review);

        return CreateReviewDTO.Response.response(savedReview);
    }
}
