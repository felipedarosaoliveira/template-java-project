package br.com.company.core.domain.usecase;

import br.com.company.core.domain.model.Message;
import br.com.company.core.domain.repository.MessageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pure Unit Test - NO Spring Framework
 * Tests domain logic in complete isolation
 *
 * Notice: No @SpringBootTest, no @MockBean
 * Just pure Java testing with manual mocks
 */
class MessageServiceTest {

    private MessageService messageService;
    private TestMessageRepository testRepository;

    @BeforeEach
    void setUp() {
        testRepository = new TestMessageRepository();
        messageService = new MessageService(testRepository);
    }

    @Test
    void shouldCreateMessageWithProcessedContent() {
        // Given
        String rawContent = "Hello World";

        // When
        Message result = messageService.createMessage(rawContent);

        // Then
        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals("Processed: Hello World", result.getContent());
        assertTrue(testRepository.wasSaveCalled());
    }

    @Test
    void shouldThrowExceptionForEmptyContent() {
        // Given
        String emptyContent = "";

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            messageService.createMessage(emptyContent);
        });
    }

    @Test
    void shouldThrowExceptionForNullContent() {
        // Given
        String nullContent = null;

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            messageService.createMessage(nullContent);
        });
    }

    @Test
    void shouldReturnMessageContentWhenFound() {
        // Given
        Message savedMessage = new Message("123", "Test content");
        testRepository.addMessage(savedMessage);

        // When
        String result = messageService.getMessage("123");

        // Then
        assertEquals("Test content", result);
    }

    @Test
    void shouldReturnDefaultMessageWhenNotFound() {
        // When
        String result = messageService.getMessage("nonexistent");

        // Then
        assertEquals("Message not found", result);
    }

    /**
     * Test Double - Manual implementation of repository for testing
     * No mocking framework needed - pure Java
     */
    private static class TestMessageRepository implements MessageRepository {
        private Message lastSaved;
        private boolean saveCalled = false;

        @Override
        public Message save(Message message) {
            this.lastSaved = message;
            this.saveCalled = true;
            return message;
        }

        @Override
        public Optional<Message> findById(String id) {
            if (lastSaved != null && lastSaved.getId().equals(id)) {
                return Optional.of(lastSaved);
            }
            return Optional.empty();
        }

        void addMessage(Message message) {
            this.lastSaved = message;
        }

        boolean wasSaveCalled() {
            return saveCalled;
        }
    }
}

