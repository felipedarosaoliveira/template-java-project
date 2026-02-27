# ✅ Architecture Compliance Checklist

Use este checklist para garantir que novas features seguem a arquitetura.

---

## 🎯 Checklist para Code Review

### Domain Layer (core/domain/)

- [ ] Classes são POJOs puros (sem anotações Spring)?
- [ ] Não há imports `org.springframework.*`?
- [ ] Não há anotações `@Service`, `@Component`, `@Repository`?
- [ ] Não há dependências de JPA (`@Entity`, `@Table`, etc.)?
- [ ] Repositories são **interfaces**, não implementações?
- [ ] Lógica de negócio está na camada correta?
- [ ] Testes são pure Java (sem `@SpringBootTest`)?

### Application Layer (application/usecase/)

- [ ] Application Services usam `@Service`?
- [ ] Application Services chamam Domain Services?
- [ ] Application Services NÃO contêm regras de negócio?
- [ ] Application Services orquestram casos de uso?
- [ ] Transações são gerenciadas aqui (se necessário)?

### Infrastructure Layer (infrastructure/)

#### Web (infrastructure/web/)
- [ ] Controllers usam `@RestController`?
- [ ] Controllers chamam Application Services (não Domain diretamente)?
- [ ] Controllers NÃO contêm lógica de negócio?
- [ ] DTOs são usados para request/response (se aplicável)?

#### Persistence (infrastructure/persistence/)
- [ ] Implementações usam `@Repository`?
- [ ] Implementam interfaces do Domain?
- [ ] NÃO contêm lógica de negócio?

#### Config (infrastructure/config/)
- [ ] Domain POJOs são instanciados como `@Bean`?
- [ ] Injeção de dependências está correta?

### Testing

- [ ] Domain tem testes unitários puros (sem Spring)?
- [ ] ArchUnit tests passam?
- [ ] Testes de integração cobrem o fluxo completo?

### Documentation

- [ ] Código está documentado com comentários claros?
- [ ] Classes complexas têm Javadoc?
- [ ] README atualizado se necessário?

---

## 🔍 Red Flags (Sinais de Violação)

### ❌ CRÍTICO - Nunca permitir

1. **Spring no Domain**
   ```java
   // ❌ ERRADO
   package br.com.company.core.domain.*;
   import org.springframework.*;  // NUNCA!
   ```

2. **Controller chamando Domain diretamente**
   ```java
   // ❌ ERRADO
   @RestController
   public class MyController {
       private final DomainService domainService;  // NUNCA!
   }
   ```

3. **Domain dependendo de Infrastructure**
   ```java
   // ❌ ERRADO
   package br.com.company.core.domain.*;
   import br.com.company.infrastructure.*;  // NUNCA!
   ```

4. **Lógica de negócio no Controller**
   ```java
   // ❌ ERRADO
   @RestController
   public class MyController {
       public Response create() {
           if (price > 100) { ... }  // Regra de negócio no controller!
       }
   }
   ```

### ⚠️ Code Smells

1. **Application Service vazio**
   ```java
   // ⚠️ Code Smell
   @Service
   public class MyUseCase {
       public Result execute() {
           return domainService.doIt();  // Só repassa?
       }
   }
   ```
   *Pode ser OK se houver justificativa (transações, logging, etc.)*

2. **Repository com lógica de negócio**
   ```java
   // ⚠️ Code Smell
   @Repository
   public class MyRepository {
       public Entity save(Entity e) {
           if (e.getPrice() > 100) { ... }  // Regra de negócio!
       }
   }
   ```

---

## 🛡️ Como Validar

### 1. Execução Manual
```bash
# Validação rápida (30 segundos)
./validate-architecture.sh

# Testes de arquitetura (1-2 minutos)
mvn test -Dtest=ArchitectureTest

# Testes completos
mvn test
```

### 2. Inspeção Visual
- Verifique que `core/pom.xml` não tem Spring
- Verifique que classes em `core/domain/` não têm anotações
- Verifique que Controllers chamam Application Services

### 3. IDE Check
- Procure imports `org.springframework` em `core/domain/`
- Deve retornar 0 resultados

---

## 🎓 Guia Rápido por Cenário

### Cenário 1: Adicionando nova Entidade

```
1. ✅ Criar em: core/domain/model/Product.java (POJO)
2. ✅ Criar interface: core/domain/repository/ProductRepository.java
3. ✅ Criar serviço: core/domain/usecase/ProductService.java (POJO)
4. ✅ Implementar repo: infrastructure/persistence/JpaProductRepository.java (@Repository)
5. ✅ Criar use case: application/usecase/CreateProductUseCase.java (@Service)
6. ✅ Criar controller: infrastructure/web/ProductController.java (@RestController)
7. ✅ Configurar bean: infrastructure/config/DomainConfiguration.java (@Bean)
```

### Cenário 2: Adicionando novo Endpoint

```
1. ✅ Adicionar método no Domain Service (core/domain/usecase/)
2. ✅ Criar Application Service (application/usecase/)
3. ✅ Criar endpoint no Controller (infrastructure/web/)
4. ✅ Testar com curl ou Postman
```

### Cenário 3: Trocando Banco de Dados

```
1. ✅ Criar nova implementação (infrastructure/persistence/MongoProductRepository.java)
2. ✅ Atualizar @Bean config (infrastructure/config/)
3. ❌ Domain NÃO precisa mudar!
4. ❌ Application NÃO precisa mudar!
```

---

## 📋 Pull Request Checklist

Antes de aprovar um PR, verifique:

- [ ] `./validate-architecture.sh` passa?
- [ ] `mvn test -Dtest=ArchitectureTest` passa?
- [ ] Não há imports Spring em `core/domain/`?
- [ ] Não há anotações Spring em classes de Domain?
- [ ] Controllers chamam Application Services?
- [ ] Lógica de negócio está no Domain?
- [ ] Testes foram adicionados?
- [ ] Documentação foi atualizada?

---

## 🚨 Ações em Caso de Violação

### Se `validate-architecture.sh` falhar:

1. **Identificar o problema** (o script mostra qual check falhou)
2. **Remover imports/anotações Spring do Domain**
3. **Mover lógica para camada correta**
4. **Re-executar validação**

### Se ArchUnit tests falharem:

1. **Ler a mensagem de erro** (indica qual regra foi violada)
2. **Revisar `docs/ARCHITECTURE-CURRENT.md`**
3. **Corrigir o código conforme a regra**
4. **Re-executar testes**

---

## 📚 Recursos de Aprendizado

### Para entender a arquitetura:
1. `docs/architecture.md` - Princípios fundamentais
2. `docs/ARCHITECTURE-VISUAL-GUIDE.md` - Diagramas e exemplos
3. `docs/ARCHITECTURE-CURRENT.md` - Referência completa

### Para desenvolver:
1. `README.md` - Quick start
2. `docs/README.md` - Índice completo
3. Exemplos de código nos arquivos existentes

---

## ✨ Lembre-se

> **"The Domain is the core of the system. All other layers exist to support it."**
> 
> — architecture.md, Section 1.5

- 🎯 Domain = Lógica de Negócio (pura)
- ⚙️ Application = Orquestração
- 🔌 Infrastructure = Adapters técnicos

**Mantenha o Domain limpo e independente!**

---

Última atualização: 27 de Fevereiro de 2026

