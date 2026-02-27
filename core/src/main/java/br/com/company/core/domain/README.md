# Domain Layer

This package contains the **core business logic** of the application.

## Structure

- **model/**: Domain entities and value objects (pure business objects)
- **repository/**: Repository interfaces (ports) - NO implementations
- **usecase/**: Domain services and use cases containing business rules

## Rules

⚠️ **CRITICAL**: This layer must be **framework-independent**

- ❌ NO Spring annotations (`@Service`, `@Component`, etc.)
- ❌ NO framework imports (Spring, JPA, etc.)
- ❌ NO dependencies on outer layers
- ✅ Pure Java POJOs only
- ✅ Can depend only on Java standard library and domain-specific libraries

## Why?

The Domain layer represents the heart of your business. It should:
- Be testable without any framework
- Be portable to any infrastructure
- Remain stable even if frameworks change
- Contain only business logic

## Dependency Direction

```
Application → Domain
Infrastructure → Domain (implements Domain interfaces)
Domain → Nothing external
```

