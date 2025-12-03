package com.providences.events.reviews.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.providences.events.organizer.dto.DashboardOrganizerDTO;
import com.providences.events.reviews.ReviewEntity;
import com.providences.events.reviews.ReviewsRepository;
import com.providences.events.reviews.ReviewEntity.ReviewTarget;
import com.providences.events.shared.dto.SystemDTO;
import com.providences.events.shared.dto.SystemDTO.ItemWithPage;
import com.providences.events.shared.exception.exceptions.BusinessException;

@Service
@Transactional(readOnly = true)
public class FetchReviewsService {
    private ReviewsRepository reviewsRepository;

    public FetchReviewsService(ReviewsRepository reviewsRepository) {
        this.reviewsRepository = reviewsRepository;
    }

    public ItemWithPage<DashboardOrganizerDTO.reviews> execute(
            int pageNumber,
            int limit,
            String sort,
            String target,
            String targetId) {

        ReviewTarget reviewTarget;
        try {
            reviewTarget = ReviewTarget.valueOf(target.toUpperCase());
        } catch (Exception e) {
            throw new BusinessException("Invalid target", HttpStatus.BAD_REQUEST);
        }

        int validPage = Math.max(pageNumber - 1, 0);
        PageRequest pageRequest = PageRequest.of(validPage, limit, Sort.by(sort).descending());

        Page<ReviewEntity> reviews = switch (reviewTarget) {
            case SERVICE -> reviewsRepository.findByService(targetId, pageRequest);
            case SUPPLIER -> reviewsRepository.findBySupplier(targetId, pageRequest);
            case ORGANIZER -> reviewsRepository.findByOrganizer(targetId, pageRequest);
        };

        System.out.println(reviews);

        List<DashboardOrganizerDTO.reviews> list = reviews.stream()
                .map(r -> new DashboardOrganizerDTO.reviews(
                        r.getId(),
                        r.getSender().name(),
                        senderName(r),
                        senderId(r),
                        r.getComment(),
                        r.getRating(),
                        r.getCreatedAt()))
                .toList();

        return new SystemDTO.ItemWithPage<>(
                list,
                reviews.getNumber() + 1,
                reviews.getTotalPages(),
                reviews.getTotalElements());

    }

    private String senderName(ReviewEntity r) {
        switch (r.getSender()) {
            case SUPPLIER:
                return r.getSenderSupplier().getCompanyName();
            case ORGANIZER:
                return r.getSenderOrganizer().getCompanyName();
            default:
                return "";
        }
    }

    private String senderId(ReviewEntity r) {
        switch (r.getSender()) {
            case SUPPLIER:
                return r.getSenderSupplier().getId();
            case ORGANIZER:
                return r.getSenderOrganizer().getId();
            default:
                return "";
        }
    }
}
