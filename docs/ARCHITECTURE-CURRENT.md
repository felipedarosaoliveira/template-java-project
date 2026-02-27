# Architecture Documentation - Current Structure

**Last Updated:** February 27, 2026  
**Status:** ✅ Compliant with Clean Architecture & Hexagonal Architecture

---

## 📐 Architectural Overview

This project follows **Clean Architecture** and **Hexagonal Architecture** principles, ensuring:
- Domain independence from frameworks
- Clear separation of concerns
- Testable business logic
- Replaceable infrastructure

---

## 📁 Project Structure

```
template/
│
├── core/                                    # Domain Layer (Framework-Independent)
│   ├── pom.xml                              # NO Spring dependencies
│   └── src/
│       ├── main/java/br/com/company/core/domain/
│       │   ├── model/                       # Domain Entities & Value Objects
│       │   │   └── Message.java             # Pure POJO entity
│       │   ├── repository/                  # Repository Interfaces (Ports)
│       │   │   └── MessageRepository.java   # Interface only
│       │   └── usecase/                     # Domain Services
│       │       ├── BusinessService.java     # Pure POJO service
│       │       └── MessageService.java      # Pure POJO service
│       └── test/java/br/com/company/core/domain/usecase/
│           ├── BusinessServiceTest.java     # Pure unit test (no Spring)
│           └── MessageServiceTest.java      # Pure unit test (no Spring)
│
└── application/                             # Infrastructure & Application Layer
    ├── pom.xml                              # Spring Boot dependencies
    └── src/
        ├── main/java/br/com/company/
        │   ├── Application.java             # Spring Boot Entry Point
        │   │
        │   ├── application/                 # Application Layer
        │   │   └── usecase/                 # Application Services (Orchestrators)
        │   │       ├── HelloUseCase.java    # @Service - orchestrates Domain
        │   │       ├── CreateMessageUseCase.java
        │   │       └── GetMessageUseCase.java
        │   │
        │   └── infrastructure/              # Infrastructure Layer (Adapters)
        │       ├── web/                     # REST Controllers (Driving Adapters)
        │       │   ├── HelloController.java
        │       │   └── MessageController.java
        │       ├── persistence/             # Repository Implementations (Driven Adapters)
        │       │   └── InMemoryMessageRepository.java
        │       └── config/                  # Spring Configuration
        │           └── DomainConfiguration.java
        │
        └── test/java/br/com/company/
            └── architecture/
                └── ArchitectureTest.java    # ArchUnit tests
```

---

## 🎯 Layer Responsibilities

### 1️⃣ Domain Layer (`core/domain/`)
**Location:** `core` module  
**Responsibility:** Business logic and domain model  
**Dependencies:** NONE (pure Java)

**Contains:**
- **model/**: Entities and Value Objects
  - Pure POJOs
  - Business rules and invariants
  - No framework annotations
  
- **repository/**: Repository Interfaces (Ports)
  - Define WHAT the domain needs
  - Don't define HOW it's implemented
  
- **usecase/**: Domain Services
  - Business logic that doesn't fit in entities
  - Orchestrate domain operations
  - Pure Java classes

**Rules:**
- ❌ NO `@Service`, `@Component`, or any Spring annotations
- ❌ NO Spring imports
- ❌ NO JPA annotations
- ❌ NO dependencies on outer layers
- ✅ Pure Java POJOs only

**Testing:**
- Pure unit tests without Spring
- Fast execution
- No mocking framework required

---

### 2️⃣ Application Layer (`application/usecase/`)
**Location:** `application` module  
**Responsibility:** Use case orchestration and coordination  
**Dependencies:** Domain layer

**Contains:**
- **usecase/**: Application Services
  - Orchestrate domain operations
  - Manage transactions
  - Handle cross-cutting concerns
  - Coordinate multiple domain services

**Rules:**
- ✅ CAN use `@Service` annotation
- ✅ CAN depend on Domain layer
- ✅ CAN use Spring features (transactions, security, etc.)
- ❌ Should NOT contain business rules (those belong in Domain)
- ❌ Should NOT access Infrastructure directly (only through Domain interfaces)

**Example:**
```java
@Service  // Spring annotation OK here
public class HelloUseCase {
    private final BusinessService businessService;  // Domain service
    
    public String execute() {
        return businessService.performBusinessLogic();
    }
}
```

---

### 3️⃣ Infrastructure Layer (`infrastructure/`)
**Location:** `application` module  
**Responsibility:** External adapters and framework configuration  
**Dependencies:** Application & Domain layers

**Contains:**

#### **web/** - Driving Adapters (Inbound)
- REST Controllers
- Translate HTTP to use case calls
- Handle request/response mapping

**Rules:**
- ✅ Use `@RestController`, `@RequestMapping`
- ✅ Call Application Services (use cases)
- ❌ Should NOT call Domain directly
- ❌ Should NOT contain business logic

**Example:**
```java
@RestController
public class HelloController {
    private final HelloUseCase helloUseCase;  // Application Service
    
    @GetMapping("/hello")
    public Map<String, String> hello() {
        return Map.of("message", helloUseCase.execute());
    }
}
```

#### **persistence/** - Driven Adapters (Outbound)
- Repository implementations
- Implement Domain repository interfaces
- Database access logic

**Rules:**
- ✅ Use `@Repository` annotation
- ✅ Implement Domain interfaces
- ✅ Use JPA, JDBC, or any persistence technology
- ❌ Should NOT contain business logic

**Example:**
```java
@Repository
public class InMemoryMessageRepository implements MessageRepository {
    // Implements interface from core/domain/repository
    @Override
    public Message save(Message message) { ... }
}
```

#### **config/** - Configuration
- Spring bean wiring
- Instantiate Domain POJOs
- Infrastructure setup

**Example:**
```java
@Configuration
public class DomainConfiguration {
    @Bean
    public BusinessService businessService() {
        return new BusinessService();  // Domain POJO
    }
    
    @Bean
    public MessageService messageService(MessageRepository repository) {
        return new MessageService(repository);  // Inject adapter
    }
}
```

---

## 🔄 Request Flow

### Example: GET /hello

```
1. HTTP Request
   ↓
2. HelloController.hello()              [infrastructure/web]
   ↓
3. HelloUseCase.execute()               [application/usecase]
   ↓
4. BusinessService.performBusinessLogic() [core/domain/usecase]
   ↓
5. Return result back through layers
```

### Example: POST /messages

```
1. HTTP POST /messages
   ↓
2. MessageController.createMessage()     [infrastructure/web]
   ↓
3. CreateMessageUseCase.execute()        [application/usecase]
   ↓
4. MessageService.createMessage()        [core/domain/usecase]
   ↓
5. MessageRepository.save()              [core/domain/repository] (interface)
   ↓
6. InMemoryMessageRepository.save()      [infrastructure/persistence] (implementation)
   ↓
7. Return Message entity through layers
```

---

## 🧩 Dependency Direction

### Compile-Time Dependencies (Source Code)

```
┌─────────────────────────────────────────┐
│   Infrastructure Layer                  │
│   (infrastructure/*)                    │
│   • @RestController, @Repository        │
│   • Spring Framework                    │
└──────────────┬──────────────────────────┘
               │ depends on
               ↓
┌─────────────────────────────────────────┐
│   Application Layer                     │
│   (application/usecase)                 │
│   • @Service (Spring)                   │
│   • Orchestrates use cases              │
└──────────────┬──────────────────────────┘
               │ depends on
               ↓
┌─────────────────────────────────────────┐
│   Domain Layer                          │
│   (core/domain)                         │
│   • Pure Java POJOs                     │
│   • NO framework dependencies           │
│   • Business logic                      │
└─────────────────────────────────────────┘
         ↑
         │ implements interfaces
         │
    Repository Adapter
    (infrastructure/persistence)
```

### Key Principle: The Dependency Rule

> **"Source code dependencies must point only inward."**

- Infrastructure → Application → Domain ✅
- Domain → Application ❌ FORBIDDEN
- Domain → Infrastructure ❌ FORBIDDEN

---

## 🔌 Ports & Adapters (Hexagonal Architecture)

### Ports (Interfaces)
Defined in **Domain layer** (`core/domain/repository/`):
```java
public interface MessageRepository {  // ← PORT
    Message save(Message message);
    Optional<Message> findById(String id);
}
```

### Adapters (Implementations)
Implemented in **Infrastructure layer** (`infrastructure/persistence/`):
```java
@Repository
public class InMemoryMessageRepository implements MessageRepository {  // ← ADAPTER
    // Implementation details
}
```

### Driving Adapters (Primary/Inbound)
Drive the application from outside:
- `infrastructure/web/` - HTTP Controllers

### Driven Adapters (Secondary/Outbound)
Driven by the application:
- `infrastructure/persistence/` - Database access
- `infrastructure/external/` - External APIs (future)

---

## 🧪 Testing Strategy

### 1. Domain Unit Tests
**Location:** `core/src/test/java/`  
**Framework:** JUnit 5 only (no Spring)  
**Speed:** Very fast (milliseconds)

```java
class MessageServiceTest {
    @Test
    void shouldCreateMessage() {
        // Pure Java - no @SpringBootTest
        MessageRepository repo = new TestRepository();
        MessageService service = new MessageService(repo);
        
        Message result = service.createMessage("test");
        
        assertNotNull(result);
    }
}
```

### 2. Architecture Tests
**Location:** `application/src/test/java/br/com/company/architecture/`  
**Framework:** ArchUnit + JUnit 5

Validates:
- Domain has no Spring dependencies
- Dependency direction is correct
- Layer boundaries are respected
- Repository interfaces in Domain, implementations in Infrastructure

### 3. Integration Tests
**Location:** `application/src/test/java/`  
**Framework:** Spring Boot Test

Tests full stack with Spring context.

---

## 📦 Maven Modules

### core (Domain)
```xml
<dependencies>
    <!-- NO Spring dependencies -->
    <!-- Only JUnit for testing -->
</dependencies>
```

### application (Infrastructure + Application)
```xml
<dependencies>
    <dependency>
        <groupId>br.com.company</groupId>
        <artifactId>core</artifactId>  <!-- Depends on Domain -->
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
</dependencies>
```

---

## ✅ Compliance Checklist

- [x] Domain layer has NO framework dependencies
- [x] Domain classes are pure POJOs (no annotations)
- [x] Repository interfaces in Domain
- [x] Repository implementations in Infrastructure
- [x] Controllers call Application Services (not Domain directly)
- [x] Application Services orchestrate Domain operations
- [x] Configuration classes wire Domain POJOs as beans
- [x] Domain unit tests don't use Spring
- [x] ArchUnit tests validate architecture rules
- [x] Dependency direction: Infrastructure → Application → Domain

**Architecture Compliance: 100% ✅**

---

## 🚀 Quick Start

### Build
```bash
mvn clean install
```

### Run
```bash
cd application
mvn spring-boot:run
```

### Test Architecture
```bash
mvn test -Dtest=ArchitectureTest
```

### Test Domain (Pure Unit Tests)
```bash
cd core
mvn test
```

### Try the API
```bash
# Basic hello endpoint
curl http://localhost:8080/hello

# Create a message
curl -X POST http://localhost:8080/messages \
  -H "Content-Type: application/json" \
  -d '{"content":"Hello Clean Architecture"}'

# Get a message (use the ID from previous response)
curl http://localhost:8080/messages/{id}
```

---

## 📚 Key Files to Review

1. **architecture.md** - Architectural principles and rules
2. **core/domain/README.md** - Domain layer guidelines
3. **application/README.md** - Application layer guidelines
4. **infrastructure/README.md** - Infrastructure layer guidelines
5. **ArchitectureTest.java** - Automated architecture validation
6. **MessageServiceTest.java** - Example of pure unit testing

---

## 🎓 Understanding the Architecture

### Why This Structure?

**Problem with traditional layered architecture:**
```
Controller → Service → Repository
```
All layers depend on frameworks, making business logic hard to test and maintain.

**Solution with Clean Architecture:**
```
Infrastructure → Application → Domain
     ↑                           ↓
     └──────── implements ───────┘
```
Domain is isolated, testable, and framework-independent.

### The Inversion

**Traditional (Bad):**
- Business logic depends on database
- Hard to test without database
- Hard to change database

**Clean Architecture (Good):**
- Database depends on business logic (through interfaces)
- Easy to test with test doubles
- Easy to swap implementations

---

## 🔍 Package Organization

| Package | Layer | Framework | Purpose |
|---------|-------|-----------|---------|
| `core.domain.model` | Domain | ❌ No | Entities & Value Objects |
| `core.domain.repository` | Domain | ❌ No | Repository Interfaces (Ports) |
| `core.domain.usecase` | Domain | ❌ No | Business Logic Services |
| `application.usecase` | Application | ✅ Yes | Use Case Orchestrators |
| `infrastructure.web` | Infrastructure | ✅ Yes | REST Controllers |
| `infrastructure.persistence` | Infrastructure | ✅ Yes | Repository Implementations |
| `infrastructure.config` | Infrastructure | ✅ Yes | Spring Configuration |

---

## 🛡️ Architecture Protection

The architecture is protected by **ArchUnit** tests that run in CI/CD:

1. **Domain Independence Test** - Ensures Domain has no Spring imports
2. **Dependency Direction Test** - Validates layers depend inward only
3. **Annotation Test** - Ensures Domain classes are POJOs
4. **Repository Test** - Validates repository implementations in Infrastructure
5. **Layer Test** - Enforces complete layer separation

**Result:** Architecture violations are caught automatically before merge.

---

## 🎯 Design Principles Applied

### 1. Dependency Rule (Clean Architecture)
✅ Dependencies point inward toward Domain

### 2. Dependency Inversion Principle (SOLID)
✅ High-level modules (Domain) don't depend on low-level modules (Infrastructure)
✅ Both depend on abstractions (interfaces)

### 3. Single Responsibility Principle (SOLID)
✅ Each layer has one clear responsibility

### 4. Open/Closed Principle (SOLID)
✅ Can add new adapters without changing Domain

### 5. Interface Segregation Principle (SOLID)
✅ Repository interfaces are focused and specific

---

## 🔄 Common Operations

### Adding a New Entity
1. Create entity in `core/domain/model/`
2. Create repository interface in `core/domain/repository/`
3. Create domain service in `core/domain/usecase/`
4. Create repository implementation in `infrastructure/persistence/`
5. Create application service in `application/usecase/`
6. Create controller in `infrastructure/web/`
7. Wire beans in `infrastructure/config/`

### Adding a New Use Case
1. Add method to domain service (`core/domain/usecase/`)
2. Create application service (`application/usecase/`)
3. Create endpoint in controller (`infrastructure/web/`)

### Changing Database Technology
1. Create new repository implementation in `infrastructure/persistence/`
2. Update bean configuration in `infrastructure/config/`
3. Domain and Application layers remain UNCHANGED

---

## 📖 Further Reading

- `docs/architecture.md` - Core architectural principles
- `docs/code-review-guidelines.md` - Code review standards
- `docs/conventions.md` - Coding conventions
- `docs/architecture-analysis-report.md` - Initial analysis results

---

## ✨ Benefits Achieved

1. **Testability** - Domain can be tested without Spring
2. **Maintainability** - Clear boundaries and responsibilities
3. **Flexibility** - Easy to change infrastructure
4. **Portability** - Business logic independent of framework
5. **Understandability** - Self-documenting structure
6. **Reliability** - Architecture validated by automated tests

---

**This architecture ensures your business logic remains stable and testable,**  
**regardless of infrastructure changes.**

