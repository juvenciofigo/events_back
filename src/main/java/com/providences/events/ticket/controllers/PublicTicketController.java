package com.providences.events.ticket.controllers;

import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.providences.events.guest.dto.GuestDTO;
import com.providences.events.ticket.dto.TicketDTO;
import com.providences.events.ticket.entities.TicketEntity;
import com.providences.events.ticket.services.GenerateTicketPdfService;
import com.providences.events.ticket.services.GetTicketByTokenService;

@Controller
@RequestMapping("/public/tickets")
public class PublicTicketController {
    private final GetTicketByTokenService getTicketByTokenService;
    private final GenerateTicketPdfService generateTicketPdfService;

    public PublicTicketController(GetTicketByTokenService getTicketByTokenService,
            GenerateTicketPdfService generateTicketPdfService) {
        this.getTicketByTokenService = getTicketByTokenService;
        this.generateTicketPdfService = generateTicketPdfService;
    }

    // Página HTML (Thymeleaf)
    @GetMapping("/{token}")
    public String viewTicketPage(@PathVariable String token, Model model) {
        TicketEntity ticket = getTicketByTokenService.execute(token);

        GuestDTO.Response guest = GuestDTO.Response.response(ticket.getGuest()) ;
        model.addAttribute("guest", guest);
        model.addAttribute("qrLink", "http://localhost:3000/public/tickets/" + token);
        return "public/original"; // Thymeleaf template
    }

    // Retorna JSON (se preferir API)
    @GetMapping("/{token}/json")
    @ResponseBody
    public ResponseEntity<?> viewTicketJson(@PathVariable String token) {
        return ResponseEntity.ok(getTicketByTokenService.execute(token));
    }

    // PDF download
    // @GetMapping("/{token}/pdf")
    // public ResponseEntity<byte[]> downloadPdf(@PathVariable String token) throws Exception {
    //     TicketEntity ticket = getTicketByTokenService.execute(token);
    //     byte[] pdf = generateTicketPdfService.generateTicketPdf(ticket);

    //     HttpHeaders headers = new HttpHeaders();
    //     headers.setContentType(MediaType.APPLICATION_PDF);
    //     headers.setContentDisposition(ContentDisposition.builder("inline")
    //             .filename(ticket.getTicketCode() + ".pdf").build());

    //     return new ResponseEntity<>(pdf, headers, HttpStatus.OK);
    // }
}
