package br.com.company.core.domain.usecase;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pure Unit Test - NO Spring Framework
 * Tests business logic in complete isolation
 */
class BusinessServiceTest {

    @Test
    void shouldPerformBusinessLogicSuccessfully() {
        // Given
        BusinessService service = new BusinessService();

        // When
        String result = service.performBusinessLogic();

        // Then
        assertNotNull(result);
        assertEquals("Business logic executed successfully!", result);
    }
}

