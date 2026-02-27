package br.com.company.core.domain.usecase;

import br.com.company.core.domain.model.Message;
import br.com.company.core.domain.repository.MessageRepository;

import java.util.UUID;

/**
 * Domain Service - Pure Java (no framework dependencies)
 * Contains business logic and orchestrates domain operations
 *
 * Note: This is framework-independent. It depends only on domain interfaces.
 */
public class MessageService {

    private final MessageRepository messageRepository;

    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    /**
     * Creates a new message with business logic
     * @param content the message content
     * @return the created message
     */
    public Message createMessage(String content) {
        // Business validation
        if (content == null || content.trim().isEmpty()) {
            throw new IllegalArgumentException("Message content cannot be empty");
        }

        // Business logic: add prefix
        String processedContent = "Processed: " + content;

        // Create domain entity
        Message message = new Message(UUID.randomUUID().toString(), processedContent);

        // Persist through repository interface (port)
        return messageRepository.save(message);
    }

    /**
     * Retrieves a message by ID
     * @param id the message ID
     * @return the message content or a default message
     */
    public String getMessage(String id) {
        return messageRepository.findById(id)
                .map(Message::getContent)
                .orElse("Message not found");
    }
}

