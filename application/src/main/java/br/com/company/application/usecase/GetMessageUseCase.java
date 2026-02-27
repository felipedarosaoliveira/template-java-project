package br.com.company.application.usecase;

import br.com.company.core.domain.usecase.MessageService;
import org.springframework.stereotype.Service;

/**
 * Application Service - Orchestrates message retrieval use case
 */
@Service
public class GetMessageUseCase {

    private final MessageService messageService;

    public GetMessageUseCase(MessageService messageService) {
        this.messageService = messageService;
    }

    public String execute(String id) {
        // Application-level orchestration
        return messageService.getMessage(id);
    }
}

