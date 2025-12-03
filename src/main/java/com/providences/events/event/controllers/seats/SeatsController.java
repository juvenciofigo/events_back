package com.providences.events.event.controllers.seats;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.providences.events.config.token.JWTUserDTO;
import com.providences.events.event.dto.SeatDTO;
import com.providences.events.event.services.seats.FetchSeatsService;
import com.providences.events.event.services.seats.GetSeatService;
import com.providences.events.shared.dto.SystemDTO;

@RestController
@RequestMapping("/seats")
public class SeatsController {
    private FetchSeatsService fetchSeatsService;
    private final GetSeatService getSeatService;

    public SeatsController(FetchSeatsService fetchSeatsService,
       GetSeatService getSeatService 
    ) {
        this.fetchSeatsService = fetchSeatsService;
       this.getSeatService = getSeatService; 
    }

    @GetMapping("/event/{eventId}")
    @PreAuthorize("hasAuthority('CLIENT')")
    public ResponseEntity<SystemDTO.ItemWithPage<SeatDTO.Response>> getSeatsByEventId(
            @PathVariable String eventId,
            @RequestParam(defaultValue = "1") int pageNumber,
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(defaultValue = "createdAt") String sort,
            Authentication authentication) {

        JWTUserDTO userData = (JWTUserDTO) authentication.getPrincipal();
        String userId = userData.getUserId();

        return ResponseEntity
                .ok()
                .body(fetchSeatsService.execute(eventId, userId, pageNumber, limit, sort));
    }

    @GetMapping("/{seatId}")
    @PreAuthorize("hasAuthority('CLIENT')")
    public ResponseEntity<SeatDTO.Response> postMethodName(
            @PathVariable("seatId") String seatId,
            Authentication authentication) {
        JWTUserDTO userData = (JWTUserDTO) authentication.getPrincipal();

        return ResponseEntity
                .ok()
                .body(getSeatService.execute(seatId, userData.getUserId()));
    }
}
