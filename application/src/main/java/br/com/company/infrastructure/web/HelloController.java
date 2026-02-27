package br.com.company.infrastructure.web;

import br.com.company.application.usecase.HelloUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Web Adapter (Infrastructure Layer)
 * Translates HTTP requests to application use cases
 * Follows: Request → Controller → Application → Domain
 */
@RestController
@RequiredArgsConstructor
public class HelloController {

    private final HelloUseCase helloUseCase;

    @GetMapping("/hello")
    public Map<String, String> hello() {
        Map<String, String> response = new HashMap<>();
        response.put("message", helloUseCase.execute());
        return response;
    }
}

