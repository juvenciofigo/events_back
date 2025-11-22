package com.providences.events.payment.services;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.providences.events.guest.GuestEntity;
import com.providences.events.guest.GuestRepository;
import com.providences.events.organizer.OrganizerEntity;
import com.providences.events.organizer.OrganizerRepository;
import com.providences.events.payment.dto.PaymentDTO;
import com.providences.events.payment.entities.PaymentEntity.PayerType;
import com.providences.events.payment.repository.PaymentRepository;
import com.providences.events.shared.exception.exceptions.BusinessException;
import com.providences.events.supplier.SupplierEntity;
import com.providences.events.supplier.SupplierRepository;

@Service
@Transactional(readOnly = true)
public class FetchPaymentsByPayerService {
    private final PaymentRepository paymentRepository;
    private final OrganizerRepository organizerRepository;
    private final SupplierRepository supplierRepository;
    private final GuestRepository guestRepository;

    public FetchPaymentsByPayerService(
            PaymentRepository paymentRepository,
            OrganizerRepository organizerRepository,
            SupplierRepository supplierRepository,
            GuestRepository guestRepository) {
        this.paymentRepository = paymentRepository;
        this.organizerRepository = organizerRepository;
        this.supplierRepository = supplierRepository;
        this.guestRepository = guestRepository;
    }

    public Set<PaymentDTO.Response> execute(String payerType, String userId, String payerId) {

        PayerType payer;
        try {
            payer = PayerType.valueOf(payerType.toUpperCase());
        } catch (Exception e) {
            throw new BusinessException("Tipo de pagador inválido", HttpStatus.BAD_REQUEST);
        }

        Set<PaymentDTO.Response> payments = switch (payer) {
            case ORGANIZER -> {
                OrganizerEntity organizer = organizerRepository.findById(payerId)
                        .orElseThrow(() -> new BusinessException("Organizador não encontrado", HttpStatus.NOT_FOUND));
                if (!organizer.getId().equals(userId)) {
                    throw new BusinessException("Organizador não possui permissão para visualizar as assinaturas",
                            HttpStatus.FORBIDDEN);
                }
                yield paymentRepository.findByPayerTypeAndOrganizer(payerId, payer).stream()
                        .map(PaymentDTO.Response::response)
                        .collect(Collectors.toSet());
            }
            case SUPPLIER -> {
                SupplierEntity supplier = supplierRepository.findById(payerId)
                        .orElseThrow(() -> new BusinessException("Fornecedor não encontrado", HttpStatus.NOT_FOUND));
                if (!supplier.getId().equals(userId)) {
                    throw new BusinessException("Fornecedor não possui permissão para visualizar as assinaturas",
                            HttpStatus.FORBIDDEN);
                }
                yield paymentRepository.findByPayerTypeAndSupplier(payerId, payer).stream()
                        .map(PaymentDTO.Response::response)
                        .collect(Collectors.toSet());
            }
            case GUEST -> {
                GuestEntity guest = guestRepository.findById(payerId)
                        .orElseThrow(() -> new BusinessException("Convidado não encontrado", HttpStatus.NOT_FOUND));
                if (!guest.getId().equals(userId)) {
                    throw new BusinessException("Convidado não possui permissão para visualizar as assinaturas",
                            HttpStatus.FORBIDDEN);
                }
                yield paymentRepository.findByPayerTypeAndGuest(payerId, payer).stream()
                        .map(PaymentDTO.Response::response)
                        .collect(Collectors.toSet());
            }
        };

        if (payments == null) {
            throw new BusinessException("Nenhuma assinatura encontrada para o usuário", HttpStatus.NOT_FOUND);
        }

        return payments;
    }
}
