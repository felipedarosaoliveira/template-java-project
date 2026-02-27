# Infrastructure Layer

This package contains **infrastructure adapters** that connect the application to external systems.

## Structure

- **web/**: HTTP/REST adapters (Controllers)
- **persistence/**: Database adapters (Repository implementations)
- **config/**: Spring configuration and bean wiring
- **messaging/**: Message queue adapters (future)
- **external/**: External API clients (future)

## Rules

✅ This layer **CAN and SHOULD** depend on frameworks

- ✅ Use Spring annotations (`@RestController`, `@Repository`, `@Configuration`)
- ✅ Use framework-specific libraries (Spring Web, JPA, etc.)
- ✅ Depends on Domain layer (implements interfaces)
- ✅ Depends on Application layer (calls use cases)
- ❌ Should NOT contain business logic

## Hexagonal Architecture (Ports & Adapters)

This layer provides **ADAPTERS** for the **PORTS** defined in Domain:

### Port (Domain)
```java
// core/domain/repository/MessageRepository.java
public interface MessageRepository { ... }  // ← PORT
```

### Adapter (Infrastructure)
```java
// infrastructure/persistence/InMemoryMessageRepository.java
@Repository
public class InMemoryMessageRepository implements MessageRepository { ... }  // ← ADAPTER
```

## Adapter Types

### 1. **Driving Adapters (Primary/Inbound)**
Adapters that **drive** the application (receive requests):
- **Web Controllers** (`infrastructure/web/`)
- **Message Listeners** (future)
- **CLI** (future)

### 2. **Driven Adapters (Secondary/Outbound)**
Adapters that are **driven by** the application (provide data/services):
- **Repository Implementations** (`infrastructure/persistence/`)
- **External API Clients** (future)
- **Email Services** (future)

## Configuration

The `config/` package is responsible for:
1. Wiring Domain POJOs as Spring beans
2. Injecting adapters into Domain services
3. Framework-level configuration (security, CORS, etc.)

**Example:**
```java
@Configuration
public class DomainConfiguration {
    @Bean
    public MessageService messageService(MessageRepository repository) {
        return new MessageService(repository);  // Inject adapter
    }
}
```

## Testing

Infrastructure components should be tested with:
- **Integration Tests** using `@SpringBootTest`
- **Controller Tests** using `@WebMvcTest`
- **Repository Tests** using `@DataJpaTest`

---

**Remember:** Infrastructure exists to serve the Domain, not the other way around!

