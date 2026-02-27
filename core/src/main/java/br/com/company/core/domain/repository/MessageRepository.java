package br.com.company.core.domain.repository;

import br.com.company.core.domain.model.Message;

import java.util.Optional;

/**
 * Repository Interface (Port) - Defined in Domain Layer
 * This is a PORT in Hexagonal Architecture
 *
 * The Domain defines WHAT it needs, not HOW it's implemented
 * Infrastructure layer will provide the ADAPTER (implementation)
 */
public interface MessageRepository {

    /**
     * Save a message
     * @param message the message to save
     * @return the saved message
     */
    Message save(Message message);

    /**
     * Find a message by its ID
     * @param id the message ID
     * @return Optional containing the message if found
     */
    Optional<Message> findById(String id);
}

