# 📖 Documentation Index

Welcome to the project documentation. This index will guide you through all architectural documentation.

---

## 🏗️ Architecture Documentation

### Core Architecture Documents

1. **[architecture.md](./architecture.md)** - 📋 **START HERE**
   - Defines the architectural principles
   - Explains the Dependency Rule
   - Outlines layer responsibilities
   - **Required reading for all developers**

2. **[ARCHITECTURE-CURRENT.md](./ARCHITECTURE-CURRENT.md)** - 📐 **Current Implementation**
   - Detailed structure of the current project
   - Layer responsibilities and examples
   - Package organization
   - Testing strategies
   - Quick start guide

3. **[ARCHITECTURE-VISUAL-GUIDE.md](./ARCHITECTURE-VISUAL-GUIDE.md)** - 🎨 **Visual Guide**
   - Visual representation of the architecture
   - Directory tree with annotations
   - Before/after comparison
   - Code examples by layer

### Analysis & Refactoring

4. **[INICIO-RAPIDO.md](./INICIO-RAPIDO.md)** - 🚀 **Quick Start (PT)**
    - Guia de início rápido em português
    - Comandos essenciais
    - Primeiros passos

---

## 👨‍💻 Development Guidelines

5. **[CHECKLIST.md](./CHECKLIST.md)** - ✅ **Compliance Checklist**
    - Architecture compliance checklist
    - Code review checklist
    - Red flags to avoid
    - Pull request checklist

6. **[conventions.md](./conventions.md)** - 📝 **Coding Conventions**
    - Code style guidelines
    - Naming conventions
    - Best practices

7. **[code-review-guidelines.md](./code-review-guidelines.md)** - 👀 **Code Review**
    - Review checklist
    - What to look for
    - Common pitfalls

---

## 🎯 Quick Navigation by Role

### For New Developers
Read in this order:
1. `INICIO-RAPIDO.md` (PT) or `README.md` (root) - Quick start
2. `architecture.md` - Understand the principles
3. `ARCHITECTURE-VISUAL-GUIDE.md` - See the structure visually
4. `ARCHITECTURE-CURRENT.md` - Detailed current state
5. `conventions.md` - Coding standards

### For Architects/Tech Leads
1. `architecture.md` - Core principles
2. `ARCHITECTURE-CURRENT.md` - Implementation details
3. Review `ArchitectureTest.java` in code

### For Code Reviewers
1. `code-review-guidelines.md` - Review standards
2. `ARCHITECTURE-CURRENT.md` - Architecture rules to verify
3. `conventions.md` - Code style to check

### For Troubleshooting Architecture Violations
1. `ARCHITECTURE-CURRENT.md` → Section "✅ Compliance Checklist"
2. Run `ArchitectureTest.java` to identify violations
3. Review `architecture.md` → Section "1.4 Boundary Enforcement"
4. Check examples in `ARCHITECTURE-VISUAL-GUIDE.md`

---

## 📚 In-Code Documentation

Additionally, each layer has a README in the source tree:

- `core/src/main/java/br/com/company/core/domain/README.md`
- `application/src/main/java/br/com/company/application/README.md`
- `application/src/main/java/br/com/company/infrastructure/README.md`

---

## 🔄 Document Status

| Document | Status | Last Updated |
|----------|--------|--------------|
| architecture.md | ✅ Original Spec | - |
| ARCHITECTURE-CURRENT.md | ✅ Up to date | Feb 27, 2026 |
| ARCHITECTURE-VISUAL-GUIDE.md | ✅ Up to date | Feb 27, 2026 |
| conventions.md | ✅ Active | - |
| code-review-guidelines.md | ✅ Active | - |

---

## 🚀 Quick Links

- **Test Architecture:** Run `mvn test -Dtest=ArchitectureTest`
- **Test Domain:** Run `cd core && mvn test`
- **Build Project:** Run `mvn clean install`
- **Start App:** Run `cd application && mvn spring-boot:run`

---

**Need help?** Start with `architecture.md` and `ARCHITECTURE-CURRENT.md`.




