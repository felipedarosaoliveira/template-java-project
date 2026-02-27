# Template Codebase - Clean Architecture Spring Boot

A multi-module Spring Boot project following **Clean Architecture** and **Hexagonal Architecture** principles.

---

## 🎯 Architecture Highlights

✅ **Domain Independence** - Business logic free from frameworks  
✅ **Hexagonal Architecture** - Ports & Adapters pattern  
✅ **Clean Architecture** - Dependency Rule strictly enforced  
✅ **Testable** - Domain tested without Spring  
✅ **Maintainable** - Clear separation of concerns  
✅ **Protected** - ArchUnit tests prevent violations  

---

## 📁 Project Structure

```
template/
├── core/          # Domain Layer (Pure Java, no frameworks)
└── application/   # Application & Infrastructure Layers (Spring Boot)
```

### Module Responsibilities

**core** - Domain Layer
- Business entities and value objects
- Repository interfaces (ports)
- Business logic services
- ❌ NO Spring dependencies

**application** - Application & Infrastructure
- Application Services (use case orchestrators)
- REST Controllers (web adapters)
- Repository implementations (persistence adapters)
- Spring configuration
- ✅ Spring Boot dependencies

---

## 🚀 Quick Start

### Prerequisites
- Java 21
- Maven 3.8+

### Build
```bash
mvn clean install
```

### Run
```bash
cd application
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

### Test
```bash
# Run all tests (including architecture tests)
mvn test

# Run only architecture validation tests
mvn test -Dtest=ArchitectureTest

# Run only domain unit tests (fast, no Spring)
cd core && mvn test
```

---

## 🌐 API Endpoints

### Hello Endpoint
```bash
curl http://localhost:8080/hello
```
**Response:**
```json
{"message": "Business logic executed successfully!"}
```

### Message Endpoints

**Create a message:**
```bash
curl -X POST http://localhost:8080/messages \
  -H "Content-Type: application/json" \
  -d '{"content":"Hello Clean Architecture"}'
```
**Response:**
```json
{
  "id": "123e4567-e89b-12d3-a456-426614174000",
  "content": "Processed: Hello Clean Architecture"
}
```

**Get a message:**
```bash
curl http://localhost:8080/messages/{id}
```

---

## 📚 Documentation

### 🎓 For New Developers
Start here: **[docs/README.md](./docs/README.md)** - Documentation index

Essential reading:
1. [architecture.md](./docs/architecture.md) - Architectural principles
2. [ARCHITECTURE-CURRENT.md](./docs/ARCHITECTURE-CURRENT.md) - Current structure
3. [ARCHITECTURE-VISUAL-GUIDE.md](./docs/ARCHITECTURE-VISUAL-GUIDE.md) - Visual guide

### 🔧 For Contributors
- [conventions.md](./docs/conventions.md) - Coding conventions
- [code-review-guidelines.md](./docs/code-review-guidelines.md) - Review guidelines

---

## 🏗️ Architecture Overview

### Layered Structure

```
┌─────────────────────────┐
│   Infrastructure        │  @RestController, @Repository
│   (Adapters)            │  Spring Framework allowed
├─────────────────────────┤
│   Application           │  @Service (orchestration)
│   (Use Cases)           │  Spring allowed
├─────────────────────────┤
│   Domain                │  Pure Java POJOs
│   (Business Logic)      │  NO frameworks
└─────────────────────────┘
```

### Dependency Direction

```
Infrastructure → Application → Domain
     ↑                          ↓
     └──── implements ───────────┘
```

The Domain defines interfaces (ports), and Infrastructure implements them (adapters).

---

## 🧪 Testing

### Domain Unit Tests (Fast)
- Pure JUnit 5
- No Spring context
- No mocking framework needed
- Execute in milliseconds

### Architecture Tests (Protection)
- ArchUnit validates architecture rules
- Runs in CI/CD pipeline
- Prevents architecture violations
- Fails build if rules broken

### Integration Tests (Full Stack)
- Spring Boot Test
- Tests complete application
- Validates wiring and configuration

---

## 🎯 Key Features

### 1. Framework Independence
The business logic is completely independent from Spring:
```java
// Domain Service (core/domain/usecase)
public class MessageService {  // Pure POJO - no @Service
    public Message createMessage(String content) {
        // Business logic here
    }
}
```

### 2. Dependency Inversion
Infrastructure depends on Domain, not the other way:
```java
// Domain defines interface (Port)
public interface MessageRepository { ... }

// Infrastructure implements it (Adapter)
@Repository
public class InMemoryMessageRepository implements MessageRepository { ... }
```

### 3. Testability
Test business logic without any framework:
```java
@Test
void test() {
    MessageRepository repo = new TestRepository();  // Manual mock
    MessageService service = new MessageService(repo);
    
    Message result = service.createMessage("test");  // Pure Java
    
    assertNotNull(result);
}
```

### 4. Replaceability
Change infrastructure without touching business logic:
- Swap in-memory repository → JPA repository
- Swap REST → GraphQL
- Swap Spring → Micronaut
- **Domain remains unchanged**

---

## 🛡️ Architecture Protection

Architecture rules are enforced by **ArchUnit** tests:

```java
@Test
void domainLayerShouldNotDependOnSpring() {
    noClasses().that().resideInAPackage("..core.domain..")
        .should().dependOnClassesThat().resideInAnyPackage("org.springframework..")
        .check(classes);
}
```

These tests run in CI/CD and **fail the build** if architecture is violated.

---

## 📦 Maven Modules

### core
- **Purpose:** Domain layer
- **Dependencies:** None (pure Java)
- **Testing:** JUnit 5

### application
- **Purpose:** Application & Infrastructure layers
- **Dependencies:** Spring Boot, core module
- **Testing:** Spring Boot Test, ArchUnit

---

## 🔄 Development Workflow

### Adding New Features

1. **Define Domain Model** (`core/domain/model/`)
   ```java
   public class Product { ... }  // Pure POJO
   ```

2. **Define Repository Interface** (`core/domain/repository/`)
   ```java
   public interface ProductRepository { ... }  // Port
   ```

3. **Implement Business Logic** (`core/domain/usecase/`)
   ```java
   public class ProductService { ... }  // Pure POJO
   ```

4. **Implement Repository** (`infrastructure/persistence/`)
   ```java
   @Repository
   public class JpaProductRepository implements ProductRepository { ... }
   ```

5. **Create Application Service** (`application/usecase/`)
   ```java
   @Service
   public class CreateProductUseCase { ... }
   ```

6. **Create Controller** (`infrastructure/web/`)
   ```java
   @RestController
   public class ProductController { ... }
   ```

7. **Configure Beans** (`infrastructure/config/`)
   ```java
   @Bean
   public ProductService productService(ProductRepository repo) {
       return new ProductService(repo);
   }
   ```

---

## ✨ Benefits

- 🎯 **Focused** - Each layer has one clear responsibility
- 🧪 **Testable** - Domain tested in isolation
- 🔄 **Flexible** - Easy to change infrastructure
- 📈 **Scalable** - Clear boundaries support growth
- 🛡️ **Protected** - Automated architecture validation
- 📚 **Understandable** - Self-documenting structure

---

## 🤝 Contributing

Before contributing:
1. Read `docs/architecture.md` - Understand the principles
2. Read `docs/conventions.md` - Follow coding standards
3. Run `mvn test` - Ensure all tests pass (including ArchUnit)
4. Follow the layer rules - Domain must remain framework-independent

---

## 📞 Getting Help

- **Architecture Questions:** Review `docs/ARCHITECTURE-CURRENT.md`
- **Visual Guide:** See `docs/ARCHITECTURE-VISUAL-GUIDE.md`
- **Conventions:** Check `docs/conventions.md`
- **Full Documentation:** Browse `docs/README.md`

---

**Built with ❤️ following Clean Architecture principles**

