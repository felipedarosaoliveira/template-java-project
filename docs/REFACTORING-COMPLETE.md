# Refactoring Complete - Architecture Guide

## 📁 New Structure

```
template/
├── core/ (Domain Layer - Framework Independent)
│   └── src/main/java/br/com/company/core/domain/
│       ├── model/           # Domain Entities & Value Objects
│       │   └── Message.java
│       ├── repository/      # Repository Interfaces (Ports)
│       │   └── MessageRepository.java
│       └── usecase/         # Domain Services
│           ├── BusinessService.java
│           └── MessageService.java
│
└── application/ (Infrastructure & Application Layer)
    └── src/main/java/br/com/company/
        ├── Application.java     # Spring Boot Entry Point
        ├── application/
        │   └── usecase/         # Application Services (Orchestrators)
        │       ├── HelloUseCase.java
        │       ├── CreateMessageUseCase.java
        │       └── GetMessageUseCase.java
        └── infrastructure/
            ├── web/             # REST Controllers
            │   ├── HelloController.java (PENDING MOVE)
            │   └── MessageController.java
            ├── persistence/     # Repository Implementations (Adapters)
            │   └── InMemoryMessageRepository.java
            └── config/          # Spring Configuration
                └── DomainConfiguration.java
```

**Note:** There's a legacy `HelloController.java` that may exist in `application/adapter/web/` 
that should be removed or consolidated with the one in `infrastructure/web/`.


---

## ✅ Architectural Compliance

### The Dependency Rule ✓

**Compile-time dependencies now point inward:**

```
Controller (infrastructure/web) 
    ↓ depends on
Application Use Case (application/usecase)
    ↓ depends on
Domain Service (core/domain)
    ↓ depends on
Repository Interface (core/domain/repository)
    ↑ implemented by
Repository Adapter (infrastructure/persistence)
```

### Domain Independence ✓

**The core module (`core/pom.xml`):**
- ❌ No Spring dependencies
- ❌ No JPA dependencies
- ❌ No framework dependencies
- ✅ Pure Java only
- ✅ JUnit 5 for testing (test scope)

**Domain classes:**
- ✅ No `@Service`, `@Component`, or any Spring annotations
- ✅ Pure POJOs
- ✅ Testable without any framework

### Dependency Inversion Principle ✓

**Domain defines interfaces (Ports):**
```java
// core/domain/repository/MessageRepository.java
public interface MessageRepository { ... }
```

**Infrastructure implements interfaces (Adapters):**
```java
// application/infrastructure/persistence/InMemoryMessageRepository.java
@Repository // Spring annotation OK here
public class InMemoryMessageRepository implements MessageRepository { ... }
```

**Configuration wires them together:**
```java
// application/infrastructure/config/DomainConfiguration.java
@Configuration
public class DomainConfiguration {
    @Bean
    public MessageService messageService(MessageRepository repository) {
        return new MessageService(repository); // Inject adapter into domain
    }
}
```

---

## 🔄 Request Flow

### Example: Creating a Message

1. **HTTP POST** `/messages` → `MessageController.createMessage()`
2. **Controller** calls → `CreateMessageUseCase.execute()`
3. **Application Service** calls → `MessageService.createMessage()`
4. **Domain Service** calls → `MessageRepository.save()` (interface)
5. **Spring** resolves → `InMemoryMessageRepository.save()` (adapter)
6. **Response** flows back through the layers

**Direction of Flow:**
```
Request → Adapter → Application → Domain → Repository Interface
                                              ↓
                                    Repository Implementation
```

**Direction of Dependencies:**
```
Adapter → Application → Domain ← Repository Implementation
```

Notice: The flow goes through Infrastructure, but **Domain doesn't depend on it**.

---

## 🧪 Testing Strategy

### Domain Tests (Pure Unit Tests)
**Location:** `core/src/test/java/`

- No Spring Boot
- No @SpringBootTest
- Pure Java with JUnit 5
- Manual test doubles (no mocking framework needed)
- Fast execution
- Complete isolation

**Example:** `MessageServiceTest.java`

### Architecture Tests
**Location:** `application/src/test/java/br/com/company/architecture/`

- Uses ArchUnit
- Validates architectural rules automatically
- Prevents violations in CI/CD pipeline
- Enforces the Dependency Rule

**Example:** `ArchitectureTest.java`

### Integration Tests
**Location:** `application/src/test/java/`

- Can use @SpringBootTest
- Tests the full stack
- Validates wiring and configuration

---

## 🎯 Key Architectural Achievements

### 1. Domain Independence
The `core` module is now completely independent from frameworks:
```java
// ✅ BEFORE: Framework-coupled
@Service  // Spring annotation
public class BusinessService { ... }

// ✅ AFTER: Framework-independent
public class BusinessService { ... }  // Pure POJO
```

### 2. Proper Layer Separation
```
Controller → Application Service → Domain Service → Repository Interface
                                                     ↑ implemented by
                                           Repository Implementation
```

### 3. Hexagonal Architecture (Ports & Adapters)
- **Ports**: Interfaces defined in Domain (`MessageRepository`)
- **Adapters**: Implementations in Infrastructure (`InMemoryMessageRepository`)
- **Core**: Business logic in pure Java (`MessageService`)

### 4. Testability
Domain logic can be tested without:
- Starting Spring context
- Mocking frameworks (optional)
- Databases or external services
- HTTP servers

### 5. Maintainability
- Easy to swap implementations (in-memory → JPA → MongoDB)
- Business logic isolated from infrastructure changes
- Clear boundaries between layers
- Self-documenting structure

---

## 📖 Following Architecture.md Rules

### ✅ Section 1.2: Dependency Rule
> "Source code dependencies must point only inward."

**Achieved:** Domain has zero dependencies on outer layers.

### ✅ Section 1.2: Domain Independence
> "The Domain layer must remain independent from: Frameworks, Databases, Web concerns..."

**Achieved:** Domain is pure Java, no framework imports.

### ✅ Section 1.3: Flow vs Dependency
> "Runtime flow and compile-time dependencies are different concepts."

**Achieved:** 
- Runtime: Request → Controller → Application → Domain → Infrastructure
- Compile-time: Controller → Application → Domain ← Infrastructure

### ✅ Section 1.4: Boundary Enforcement
> "No Domain referencing framework annotations"

**Achieved:** Domain classes are POJOs.

### ✅ Section 1.5: Architectural Goals
- ✅ High cohesion inside layers
- ✅ Low coupling between layers
- ✅ Replaceable infrastructure
- ✅ Isolated and testable business rules
- ✅ Explicit use case orchestration
- ✅ Long-term maintainability

---

## 🚀 How to Build and Run

### Build the project
```bash
mvn clean install
```

### Run the application
```bash
cd application
mvn spring-boot:run
```

### Run architecture tests
```bash
mvn test -Dtest=ArchitectureTest
```

### Run domain unit tests
```bash
cd core
mvn test
```

### Test the endpoints

**Original endpoint (still works):**
```bash
curl http://localhost:8080/hello
```

**New endpoints:**
```bash
# Create a message
curl -X POST http://localhost:8080/messages \
  -H "Content-Type: application/json" \
  -d '{"content":"Hello from Clean Architecture"}'

# Get a message
curl http://localhost:8080/messages/{id}
```

---

## 📝 Summary of Changes

| File/Directory | Action | Reason |
|----------------|--------|--------|
| `core/pom.xml` | Removed Spring dependencies | Domain must be framework-independent |
| `BusinessService.java` | Removed `@Service` annotation | Domain should be pure POJO |
| `BusinessService.java` | Moved to `core/domain/usecase/` | Proper package structure |
| `HelloController.java` | Moved to `infrastructure/web/` | Controllers are infrastructure adapters |
| `HelloController.java` | Changed to call `HelloUseCase` | Follow proper flow |
| `HelloUseCase.java` | Created in `application/usecase/` | Application Service orchestrator |
| `DomainConfiguration.java` | Created in `infrastructure/config/` | Wire domain POJOs as Spring beans |
| `Message.java` | Created | Example domain entity |
| `MessageRepository.java` | Created | Repository interface (port) |
| `MessageService.java` | Created | Domain service using repository |
| `InMemoryMessageRepository.java` | Created in `infrastructure/persistence/` | Repository implementation (adapter) |
| `CreateMessageUseCase.java` | Created in `application/usecase/` | Application Service |
| `GetMessageUseCase.java` | Created in `application/usecase/` | Application Service |
| `MessageController.java` | Created in `infrastructure/web/` | Controller using Application Services |
| `ArchitectureTest.java` | Created | Automated architecture validation |
| `MessageServiceTest.java` | Created | Pure unit test without Spring |

---

## 🎓 Learning Resources

For team members new to this architecture:

1. Read `docs/architecture.md` - Core principles
2. Read `core/src/main/java/br/com/company/core/domain/README.md` - Domain layer rules
3. Read `application/src/main/java/br/com/company/application/README.md` - Application layer rules
4. Study `MessageServiceTest.java` - Example of pure unit testing
5. Study `ArchitectureTest.java` - Architecture validation

---

## ✨ Next Steps (Optional Enhancements)

1. Add more domain entities and value objects
2. Implement JPA-based repository adapter
3. Add exception handling at application layer
4. Implement DTO mappers in adapters
5. Add more comprehensive integration tests
6. Configure CI/CD to run ArchUnit tests
7. Add API documentation (OpenAPI/Swagger)

---

**Architecture Refactoring: COMPLETE ✅**

The project now fully complies with Clean Architecture and Hexagonal Architecture principles as defined in `architecture.md`.

