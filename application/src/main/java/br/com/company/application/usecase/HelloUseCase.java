package br.com.company.application.usecase;

import br.com.company.core.domain.usecase.BusinessService;
import org.springframework.stereotype.Service;

/**
 * Application Service - Orchestrates use case execution
 * This layer can depend on frameworks (Spring) and coordinates Domain calls
 */
@Service
public class HelloUseCase {

    private final BusinessService businessService;

    public HelloUseCase(BusinessService businessService) {
        this.businessService = businessService;
    }

    public String execute() {
        // Orchestrate domain logic
        // Could include transaction management, logging, security checks, etc.
        return businessService.performBusinessLogic();
    }
}

