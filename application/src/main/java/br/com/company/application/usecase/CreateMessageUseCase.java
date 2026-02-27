package br.com.company.application.usecase;

import br.com.company.core.domain.model.Message;
import br.com.company.core.domain.usecase.MessageService;
import org.springframework.stereotype.Service;

/**
 * Application Service - Orchestrates message creation use case
 * This layer can depend on frameworks (Spring) and coordinates Domain calls
 *
 * Responsibilities:
 * - Transaction management
 * - Cross-cutting concerns (logging, security, etc.)
 * - Orchestration of multiple domain operations
 */
@Service
public class CreateMessageUseCase {

    private final MessageService messageService;

    public CreateMessageUseCase(MessageService messageService) {
        this.messageService = messageService;
    }

    public Message execute(String content) {
        // Application-level orchestration
        // Could include: transaction management, event publishing, logging, etc.

        return messageService.createMessage(content);
    }
}

