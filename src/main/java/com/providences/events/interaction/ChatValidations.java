package com.providences.events.interaction;

import com.providences.events.interaction.entities.ChatEntity.ChatType;
import com.providences.events.shared.exception.exceptions.BusinessException;
import org.springframework.http.HttpStatus;

public class ChatValidations {
    private ChatValidations() {
    }

    public static ChatType parseChatType(String type) {
        if (type == null || type.isBlank()) {
            throw new BusinessException("Tipo é obrigatório", HttpStatus.BAD_REQUEST);
        }

        try {
            return ChatType.valueOf(type.toUpperCase());
        } catch (Exception e) {
            throw new BusinessException("Tipo de chat inválido", HttpStatus.BAD_REQUEST);
        }
    }
}
