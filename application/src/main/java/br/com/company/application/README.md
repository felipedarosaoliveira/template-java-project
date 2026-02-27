# Application Layer

This package contains the **application orchestration logic**.

## Structure

- **usecase/**: Application Services that orchestrate domain use cases
- **../infrastructure/**: Infrastructure adapters for external systems
  - **web/**: REST Controllers and HTTP adapters
  - **persistence/**: Repository implementations (JPA, JDBC, etc.)
  - **config/**: Spring configuration classes

## Rules

✅ This layer **CAN** depend on frameworks (Spring, etc.)

- ✅ Can use Spring annotations
- ✅ Can orchestrate transactions
- ✅ Can handle cross-cutting concerns (logging, security)
- ✅ Depends on Domain layer
- ❌ Should NOT contain business rules (those belong in Domain)

## Responsibilities

### Use Cases (Application Services)
- Orchestrate Domain calls
- Manage transactions
- Handle application-level concerns
- Coordinate multiple domain operations

### Infrastructure Adapters
- **Web Adapters**: Translate HTTP requests to use case calls
- **Persistence Adapters**: Implement Domain repository interfaces
- **External Service Adapters**: Integrate with external APIs

## Flow

```
HTTP Request → Controller (infrastructure/web) 
            → Use Case (application/usecase/) 
            → Domain Service (core/domain)
            → Repository Interface (core/domain/repository)
            → Repository Implementation (infrastructure/persistence)
```

