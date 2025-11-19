# Getting Started

api do aplicativo de eventos

## ToDo

### Autenticação e Usuários

    [] POST /auth/register
    [] POST /auth/login
    [] POST /auth/logout
    [] POST /auth/refresh-token
    [] POST /auth/forgot-password
    [] POST /auth/reset-password
    [] GET /auth/me

### users

    [] GET /users
    [] GET /users/{id}
    [] PATCH /users/{id}
    [] DELETE /users/{id}
    [] PATCH /users/{id}/role (organizer, supplier, guest)
    [] GET /users/{id}/permissions

### Organizador

    [x] POST /organizers — criar conta de organizador
    [x] GET /organizers/{id}
    [x] PATCH /organizers/{id}
    [] DELETE /organizers/{id}
    [x] GET /organizers/{id}/events — listar eventos do organizador
    [] GET /organizers — listar organizadores

### Prestador de serviços

    [x] POST /suppliers — cadastro do fornecedor
    [x] GET /suppliers/{id}
    [x] PATCH /suppliers/{id}
    [] GET /suppliers — listar fornecedores
    [x] GET /suppliers/{id}/services — serviços do fornecedor

#### Serviços

    [x] POST /services
    [x] PATCH /services/{id}
    [x] DELETE /services/{id}
    [x] GET /services/{id}
    [] DELETE /suppliers/{id}
    [] GET /services?category={categoria}&city={cidade}
    [x] POST /services/{id}/albums — criar álbum
    [x] POST /services/{id}/albums/{albumId}/photos — upload de fotos

### Evento

    [x] POST /events
    [x] PATCH /events/{id}
    [] DELETE /events/{id}
    [x] GET /events/{id}
    [x] GET /events?public=true — eventos públicos
    [x] GET /organizers/{id}/events — eventos do organizador

### Configurações do Evento

    [] GET /events/{id}/summary
    [] POST /events/{id}/publish
    [] POST /events/{id}/unpublish
    [] GET /events/{id}/dashboard (estatísticas)

### Convidados (Guests)

    [] POST /guests
    [] POST /guests/purchase — comprar ticket
    [] GET /events/{id}/guests - convidados
    [] GET /guests/{id}
    [] GET /guest/me/
    [] PATCH /guests/{id}
    [] DELETE /guests/{id}

### Tickets

    [] POST /tickets
    [] GET /tickets/{id}
    [] GET /events/{id}/tickets — todos tickets do evento
    [] PATCH /tickets/{id}
    [] DELETE /tickets/{id}

#### Ticket Types

    [] POST /events/{id}/ticket-types
    [] GET /events/{id}/ticket-types
    [] PATCH /ticket-types/{id}
    [] DELETE /ticket-types/{id}

### Convites & RSVP

    [] POST /events/{id}/invite
    [] GET /events/{id}/invites
    [] PATCH /invites/{id}/status (aceitar/recusar)
    [] GET /invites/my

### Seat Types

    [] POST /seat-types
    [] GET /seat-types/{eventId}
    [] PATCH /seat-types/{id}
    [] DELETE /seat-types/{id}

#### Assentos (Seats)

        [] POST /seats — criar tipo de assento
        [] PATCH /seats/{id}
        [] DELETE /seats/{id}
        [] GET /events/{id}/seats
        [] POST /seats/{id}/reserve — reservar assentos
        [] POST /seats/{id}/release — liberar assentos

### Chat

    [] POST /chats/ — iniciar conversa(organizer, supplier, guest)
    [] GET /chats/{id}
    [] GET /chats/my/{id} — listar chats do usuário (guest/supplier/organizer)

#### Mensagens

    [] GET /chats/{chatId}/messages
    [] POST /chats/{chatId}/messages

### Pagamentos

    [] POST /payments/initiate — iniciar pagamento
    [] POST /payments/callback — webhook M-Pesa/Paypal/Evolution
    [] GET /payments
    [] GET /payments/{id}
    [] PATCH /payments/{id}
    [] GET /payments/{supplierId}
    [] GET /payments/{organizerId}
    [] GET /payments/{id}/refund

#### Referências de pagamento

    [] POST /payment-references
    [] GET /payment-references/{id}
    [] GET /payment-references/by-reference/{ref}

### Planos, Subscrições e Add-ons

    [] GET /plans?type=organizer
    [] GET /plans?type=supplier
    [] GET /plans?type=addon
    [] POST /plans?type=?
    [] PATCH /plans/{id}/?type=?
    [] DELETE /plans/{id}/?type=?
    [] GET /plans/{id}

### Subscrições

    [] POST /subscriptions — assinar plano (organizer / supplier / addon)
    [] GET /subscriptions/{id}
    [] GET /subscriptions/my
    [] GET /subscriptions
    [] PATCH /subscriptions/{id}
    [] PATCH /subscriptions/{id}/cancel
    [] PATCH /subscriptions/{id}/renew

### Relatórios

    [] GET /reports/events/{id}/sales
    [] GET /reports/events/{id}/attendance
    [] GET /reports/suppliers/{id}/services
    [] GET /reports/platform/finance

### Convites (WhatsApp / Evolution API)

    [] POST /invites/send — enviar convite via Evolution
    [] POST /invites/bulk — enviar em massa
    [] GET /invites/{eventId} — listar convites enviados

### Notificações

    [] GET /notifications
    [] PATCH /notifications/{id}/read
    [] POST /notifications/send

### Configurações da Plataforma

    [] GET /platform/settings
    [] PATCH /platform/settings
    [] GET /platform/integrations

### Upload de Arquivos (Fotos / Banners / Álbuns)

    [] POST /upload/image
    [] POST /upload/album/{id}

### 16. Dashboard & Relatórios

    [] GET /dashboard/organizer/{id}
    [] GET /dashboard/supplier/{id}
    [] GET /dashboard/system-admin
    [] GET /reports/{eventId}/tickets
    [] GET /reports/{eventId}/sales
    [] GET /reports/{eventId}/participants

### Administração do sistema

    [] GET /admin/users
    [] GET /admin/events
    [] GET /admin/payments
    [] GET /admin/plans
    [] GET /admin/issues (suporte)
    [] POST /admin/maintenance-mode
