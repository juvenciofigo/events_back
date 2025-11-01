package com.providences.events.payment;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.providences.events.guest.GuestEntity;
import com.providences.events.organizer.OrganizerEntity;
import com.providences.events.plans.entities.SubscriptionEntity;
import com.providences.events.services.ServiceEntity;
import com.providences.events.supplier.SupplierEntity;
import com.providences.events.ticket.TicketEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@ToString(onlyExplicitlyIncluded = true)
@Table(name = "payments")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "payment_method")
    private PaymentMethod paymentMethod;

    @Column(precision = 10, scale = 2)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Currency currency;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status = Status.PENDING;

    private String description;

    @Column(nullable = false, updatable = false, name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(nullable = false, name = "update_at")
    private LocalDateTime updatedAt;

    // Payer
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "payer_type")
    private PayerType payerType;

    @ManyToOne
    @JoinColumn(name = "payer_supplier_id")
    private SupplierEntity payerSupplier;

    @ManyToOne
    @JoinColumn(name = "payer_organizer_id")
    private OrganizerEntity payerOrganizer;

    @ManyToOne
    @JoinColumn(name = "payer_guest_id")
    private GuestEntity payerGuest;

    // Receiver

    @Column(nullable = false, name = "receiver_type")
    @Enumerated(EnumType.STRING)
    private ReceiverType receiverType;

    @ManyToOne
    @JoinColumn(name = "receiver_supplier_id")
    private SupplierEntity receiverSupplier;

    @ManyToOne
    @JoinColumn(name = "receiver_organizer_id")
    private OrganizerEntity receiverOrganizer;

    @Column(name = "receiver_platform")
    private Boolean receiverPlatform;

    // Target
    @Column(nullable = false, name = "payment_target")
    @Enumerated(EnumType.STRING)
    private Target target;

    @ManyToOne
    @JoinColumn(name = "services_id")
    private ServiceEntity service;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subscription_id")
    private SubscriptionEntity subscription;

    @OneToOne
    @JoinColumn(name = "ticket_id")
    private TicketEntity ticket;

    // Método para identificar quem fez o pagamento
    public String getPayer() {
        if (payerSupplier != null) {
            return "Fornecedor: " + payerSupplier.getCompanyName();
        } else if (payerOrganizer != null) {
            return "Organizador: " + payerOrganizer.getName();
        } else if (payerGuest != null) {
            return "Convidado: " + payerGuest.getName();
        }
        return "Não identificado";
    }

    @PrePersist
    @PreUpdate
    private void validatePayment() {
        currency = Currency.MZN;
        updatedAt = LocalDateTime.now();
        // Payer
        int payerCount = 0;
        if (payerSupplier != null)
            payerCount++;
        if (payerOrganizer != null)
            payerCount++;
        if (payerGuest != null)
            payerCount++;
        if (payerCount > 1)
            throw new IllegalStateException("Um pagamento não pode ter mais de um pagador definido");
        if (payerCount == 0)
            throw new IllegalStateException("Um pagamento deve ter um pagador definido");

        // Receiver
        int receiverCount = 0;
        if (receiverSupplier != null)
            receiverCount++;
        if (receiverOrganizer != null)
            receiverCount++;
        if (receiverPlatform != null && receiverPlatform)
            receiverCount++;
        if (receiverCount > 1)
            throw new IllegalStateException("Um pagamento não pode ter mais de um receptor definido");
        if (receiverCount == 0)
            throw new IllegalStateException("Um pagamento deve ter um receptor definido");
    }

    public enum PayerType {
        ORGANIZER,
        SUPPLIER,
        GUEST
    }

    public enum Currency {
        MZN

    }

    public enum PaymentMethod {
        MPESA,
        MKESH,
        EMOLA,
        CARD,
        PAYPAL
    }

    public enum Status {
        PENDING,
        COMPLETED,
        FAILED,
        REFUNDED
    }

    public enum Target {
        SUBSCRIPTION,
        SERVICE,
        TICKET
    }

    public enum ReceiverType {
        PLATFORM,
        ORGANIZER,
        SUPPLIER

    }
}
