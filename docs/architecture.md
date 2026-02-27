# 1. Architectural Principle

## 1.1 Architectural Style

This system follows a layered architecture inspired by Clean Architecture and Hexagonal Architecture principles.

The system is organized around a central business core (Domain), surrounded by application orchestration and external adapters.

The architecture enforces strict separation of concerns and explicit dependency boundaries.

---

## 1.2 Dependency Rule

The most important rule of this architecture is:

> Source code dependencies must point only inward.

This means:

- Controller depends on Application.
- Application depends on Domain.
- Infrastructure depends on Domain.
- Domain depends on nothing external.

The Domain layer must remain independent from:
- Frameworks
- Databases
- Web concerns
- Infrastructure
- Application orchestration

No code inside the Domain layer may import classes from outer layers.

---

## 1.3 Direction of Flow vs Direction of Dependency

Runtime flow and compile-time dependencies are different concepts.

Runtime execution flow:

Request → Controller → Application → Domain → Repository → Infrastructure

Compile-time dependency direction:

Controller → Application → Domain  
Infrastructure → Domain  

The Domain layer does not depend on Infrastructure.
Instead, Infrastructure implements interfaces defined in the Domain.

This ensures that business logic remains stable even if frameworks or databases change.

---

## 1.4 Boundary Enforcement

Each layer has a clearly defined responsibility and boundary.

Violations include:

- Domain referencing framework annotations
- Controller accessing repositories directly
- Application containing business rules that belong in Domain
- Infrastructure defining business logic

Boundaries must be respected in both structure and behavior.

---

## 1.5 Architectural Goals

This architecture is designed to achieve:

- High cohesion inside layers
- Low coupling between layers
- Replaceable infrastructure
- Isolated and testable business rules
- Explicit use case orchestration
- Long-term maintainability

The Domain is the core of the system.
All other layers exist to support it.