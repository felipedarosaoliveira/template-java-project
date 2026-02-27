package br.com.company.infrastructure.web;

import br.com.company.application.usecase.CreateMessageUseCase;
import br.com.company.application.usecase.GetMessageUseCase;
import br.com.company.core.domain.model.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Web Adapter (Infrastructure Layer)
 * Translates HTTP requests to application use cases
 * Follows: Request → Controller → Application Use Case → Domain
 */
@RestController
@RequestMapping("/messages")
@RequiredArgsConstructor
public class MessageController {

    private final CreateMessageUseCase createMessageUseCase;
    private final GetMessageUseCase getMessageUseCase;

    @PostMapping
    public ResponseEntity<Map<String, String>> createMessage(@RequestBody Map<String, String> request) {
        String content = request.get("content");

        Message message = createMessageUseCase.execute(content);

        Map<String, String> response = new HashMap<>();
        response.put("id", message.getId());
        response.put("content", message.getContent());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, String>> getMessage(@PathVariable String id) {
        String content = getMessageUseCase.execute(id);

        Map<String, String> response = new HashMap<>();
        response.put("content", content);

        return ResponseEntity.ok(response);
    }
}

