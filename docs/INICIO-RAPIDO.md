# 🚀 Guia de Início Rápido

**Bem-vindo ao Template Clean Architecture!**

Este guia vai te ajudar a começar em **5 minutos**.

---

## ⚡ Quick Start (3 comandos)

```bash
# 1. Validar arquitetura
./validate-architecture.sh

# 2. Compilar e instalar
mvn clean install

# 3. Executar aplicação
cd application && mvn spring-boot:run
```

**Pronto!** Aplicação rodando em `http://localhost:8080` 🎉

---

## 🧪 Testar API

### Endpoint Hello
```bash
curl http://localhost:8080/hello
```

**Resposta esperada:**
```json
{"message": "Business logic executed successfully!"}
```

### Criar Mensagem
```bash
curl -X POST http://localhost:8080/messages \
  -H "Content-Type: application/json" \
  -d '{"content":"Minha primeira mensagem"}'
```

**Resposta esperada:**
```json
{
  "id": "abc-123-def-456",
  "content": "Processed: Minha primeira mensagem"
}
```

### Buscar Mensagem
```bash
# Use o ID retornado no comando anterior
curl http://localhost:8080/messages/abc-123-def-456
```

---

## 📖 Entender o Projeto (10 minutos)

### Passo 1: Leia o Resumo
📄 Abra: `docs/RESUMO-EXECUTIVO-PT.md`

**O que você vai aprender:**
- Status da refatoração
- O que foi mudado
- Como validar
- Estrutura do projeto

### Passo 2: Veja a Estrutura Visual
📄 Abra: `docs/ARCHITECTURE-VISUAL-GUIDE.md`

**O que você vai ver:**
- Diagramas da arquitetura
- Fluxo de requisições
- Exemplos de código
- Antes vs Depois

### Passo 3: Explore o Código

**Domain (Pure Java):**
```bash
# Ver entidade de domínio
cat core/src/main/java/br/com/company/core/domain/model/Message.java

# Ver serviço de domínio (sem anotações!)
cat core/src/main/java/br/com/company/core/domain/usecase/BusinessService.java

# Ver interface de repositório (Port)
cat core/src/main/java/br/com/company/core/domain/repository/MessageRepository.java
```

**Infrastructure:**
```bash
# Ver controller (Adapter)
cat application/src/main/java/br/com/company/infrastructure/web/HelloController.java

# Ver implementação de repositório (Adapter)
cat application/src/main/java/br/com/company/infrastructure/persistence/InMemoryMessageRepository.java

# Ver configuração (bean wiring)
cat application/src/main/java/br/com/company/infrastructure/config/DomainConfiguration.java
```

---

## 🎯 Conceitos Principais (5 minutos)

### 1. Domain é POJO Puro
```java
// ✅ CORRETO (sem anotações)
public class BusinessService {
    public String performBusinessLogic() { ... }
}
```

### 2. Infrastructure usa Spring
```java
// ✅ CORRETO (com anotações)
@Configuration
public class DomainConfiguration {
    @Bean
    public BusinessService businessService() {
        return new BusinessService();  // Instancia POJO
    }
}
```

### 3. Fluxo de Chamadas
```
HTTP → Controller → Application Service → Domain Service
```

### 4. Dependency Direction
```
Infrastructure → Application → Domain
```

---

## 🛠️ Primeiro Desenvolvimento

### Adicionar Novo Endpoint (15 minutos)

#### 1. Criar método no Domain
```java
// core/domain/usecase/BusinessService.java
public String sayGoodbye() {
    return "Goodbye from Domain!";
}
```

#### 2. Criar Application Service
```java
// application/usecase/GoodbyeUseCase.java
package br.com.company.application.usecase;

import br.com.company.core.domain.usecase.BusinessService;
import org.springframework.stereotype.Service;

@Service
public class GoodbyeUseCase {
    private final BusinessService businessService;
    
    public GoodbyeUseCase(BusinessService businessService) {
        this.businessService = businessService;
    }
    
    public String execute() {
        return businessService.sayGoodbye();
    }
}
```

#### 3. Adicionar endpoint no Controller
```java
// infrastructure/web/HelloController.java
private final GoodbyeUseCase goodbyeUseCase;

@GetMapping("/goodbye")
public Map<String, String> goodbye() {
    return Map.of("message", goodbyeUseCase.execute());
}
```

#### 4. Atualizar constructor
```java
public HelloController(HelloUseCase helloUseCase, GoodbyeUseCase goodbyeUseCase) {
    this.helloUseCase = helloUseCase;
    this.goodbyeUseCase = goodbyeUseCase;
}
```

#### 5. Testar
```bash
mvn clean install
cd application && mvn spring-boot:run
curl http://localhost:8080/goodbye
```

**Pronto!** Novo endpoint funcionando! 🎉

---

## 🧪 Validar Mudanças

Sempre que fizer mudanças, valide:

```bash
# Validação rápida (30s)
./validate-architecture.sh

# Testes de arquitetura (2min)
mvn test -Dtest=ArchitectureTest

# Todos os testes
mvn test
```

---

## ❌ O Que NÃO Fazer

### NUNCA adicione Spring no Domain!
```java
// ❌ ERRADO
package br.com.company.core.domain.usecase;
import org.springframework.stereotype.Service;  // ← NUNCA!

@Service  // ← NUNCA!
public class MyService { ... }
```

### NUNCA chame Domain direto do Controller!
```java
// ❌ ERRADO
@RestController
public class MyController {
    private final DomainService domainService;  // ← NUNCA!
}
```

### NUNCA coloque lógica de negócio no Controller!
```java
// ❌ ERRADO
@RestController
public class MyController {
    @PostMapping
    public Response create() {
        if (price > 100) { ... }  // ← Regra de negócio! NUNCA!
    }
}
```

---

## ✅ O Que FAZER

### ✅ Domain é POJO puro
```java
// ✅ CORRETO
package br.com.company.core.domain.usecase;

public class MyService {  // Sem anotações!
    public Result doBusinessLogic() { ... }
}
```

### ✅ Controller chama Application Service
```java
// ✅ CORRETO
@RestController
public class MyController {
    private final MyUseCase useCase;  // Application Service!
}
```

### ✅ Lógica de negócio no Domain
```java
// ✅ CORRETO (no Domain)
public class ProductService {
    public void validatePrice(BigDecimal price) {
        if (price.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Price must be positive");
        }
    }
}
```

---

## 📚 Próximos Passos

### Para Aprender Mais (30 minutos)
1. 📖 `docs/ARCHITECTURE-CURRENT.md` - Estrutura completa
2. 📖 `docs/ARCHITECTURE-VISUAL-GUIDE.md` - Diagramas
3. 📖 `docs/architecture.md` - Princípios fundamentais

### Para Desenvolver
1. 📋 Use `docs/CHECKLIST.md` em code reviews
2. 📋 Siga exemplos nos arquivos existentes
3. 📋 Valide sempre com `./validate-architecture.sh`

### Para Treinar o Time
1. 🎓 Compartilhe `docs/RESUMO-EXECUTIVO-PT.md`
2. 🎓 Faça demo do fluxo de requisição
3. 🎓 Mostre como testar Domain sem Spring

---

## 🎯 Comandos Mais Usados

```bash
# Validar arquitetura
./validate-architecture.sh

# Compilar
mvn clean install

# Rodar
cd application && mvn spring-boot:run

# Testar Domain (rápido)
cd core && mvn test

# Testar Arquitetura
mvn test -Dtest=ArchitectureTest

# Testar API
curl http://localhost:8080/hello
```

---

## 💡 Dicas

### ✅ Sempre valide após mudanças
```bash
./validate-architecture.sh  # Leva 30 segundos
```

### ✅ Teste Domain sem Spring
```bash
cd core && mvn test  # Leva segundos, não minutos
```

### ✅ Use os exemplos existentes
- Copie estrutura de `MessageService`
- Siga padrão de `MessageController`
- Replique configuração de `DomainConfiguration`

### ✅ Leia os comentários no código
Todos os arquivos têm comentários explicativos!

---

## 🏆 Você Agora Tem

✅ Um projeto 100% conforme Clean Architecture  
✅ Domain independente de frameworks  
✅ Testes rápidos e eficientes  
✅ Proteção contra violações arquiteturais  
✅ Documentação extensiva  
✅ Template de referência  

---

## 🎉 Pronto!

Você já pode:
1. ✅ Executar o projeto
2. ✅ Testar os endpoints
3. ✅ Começar a desenvolver
4. ✅ Validar a arquitetura

**Dúvidas?** Consulte `docs/README.md` para índice completo.

---

**Bom desenvolvimento! 🚀**

