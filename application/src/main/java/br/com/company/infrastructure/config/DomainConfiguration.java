package br.com.company.infrastructure.config;

import br.com.company.core.domain.repository.MessageRepository;
import br.com.company.core.domain.usecase.BusinessService;
import br.com.company.core.domain.usecase.MessageService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Infrastructure Configuration
 * Responsible for instantiating Domain objects as Spring beans
 * This keeps the Domain layer free from framework dependencies
 *
 * Following Dependency Inversion Principle:
 * - Domain defines interfaces (ports)
 * - Infrastructure implements interfaces (adapters)
 * - Configuration wires them together
 */
@Configuration
public class DomainConfiguration {

    /**
     * Creates BusinessService as a Spring bean
     * The Domain object itself has no Spring annotations
     */
    @Bean
    public BusinessService businessService() {
        return new BusinessService();
    }

    /**
     * Creates MessageService as a Spring bean
     * Injects the MessageRepository implementation (adapter)
     */
    @Bean
    public MessageService messageService(MessageRepository messageRepository) {
        return new MessageService(messageRepository);
    }
}


