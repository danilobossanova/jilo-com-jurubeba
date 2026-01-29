# Arquitetura do Projeto jilo-com-jurubeba

> @author Danilo Fernando

## Visão Geral

O projeto jilo-com-jurubeba segue os princípios da **Clean Architecture** (também conhecida como Hexagonal Architecture ou Ports & Adapters), proposta por Robert C. Martin (Uncle Bob).

O objetivo principal é criar um sistema onde as **regras de negócio são o centro** da aplicação, completamente isoladas de detalhes de implementação como frameworks, banco de dados e interfaces de usuário.

## Princípios Fundamentais

### 1. Independência de Frameworks
O código de negócio não depende de Spring, JPA ou qualquer outro framework. Frameworks são detalhes de implementação.

### 2. Testabilidade
As regras de negócio podem ser testadas sem UI, banco de dados, servidor web ou qualquer elemento externo.

### 3. Independência de UI
A interface do usuário pode mudar facilmente, sem alterar o restante do sistema. Uma API REST pode ser substituída por GraphQL sem impacto no domínio.

### 4. Independência de Banco de Dados
PostgreSQL pode ser trocado por MongoDB sem alterar as regras de negócio. O banco é um detalhe de implementação.

### 5. Independência de Agentes Externos
As regras de negócio não sabem nada sobre interfaces externas (APIs de terceiros, filas, etc).

---

## Diagrama de Camadas

```mermaid
graph TB
    subgraph "Camadas da Clean Architecture"
        direction TB

        subgraph "🔵 Interfaces (Adaptadores de Entrada)"
            REST[REST Controllers]
            CLI[CLI Commands]
            GRAPHQL[GraphQL Resolvers]
        end

        subgraph "🟢 Application (Casos de Uso)"
            UC[Use Cases]
            DTO[DTOs Input/Output]
        end

        subgraph "🟡 Domain (Regras de Negócio)"
            ENT[Entities]
            VO[Value Objects]
            GW[Gateway Interfaces]
            EXC[Domain Exceptions]
        end

        subgraph "🔴 Infrastructure (Adaptadores de Saída)"
            REPO[Repositories]
            JPA[JPA Entities]
            CONFIG[Configurations]
            EXT[External Services]
        end
    end

    REST --> UC
    CLI --> UC
    GRAPHQL --> UC
    UC --> ENT
    UC --> GW
    REPO -.->|implementa| GW
    REPO --> JPA

    style REST fill:#4A90D9
    style CLI fill:#4A90D9
    style GRAPHQL fill:#4A90D9
    style UC fill:#7CB342
    style DTO fill:#7CB342
    style ENT fill:#FDD835
    style VO fill:#FDD835
    style GW fill:#FDD835
    style EXC fill:#FDD835
    style REPO fill:#EF5350
    style JPA fill:#EF5350
    style CONFIG fill:#EF5350
    style EXT fill:#EF5350
```

---

## Fluxo de uma Requisição

```mermaid
sequenceDiagram
    participant C as Cliente HTTP
    participant CT as Controller
    participant UC as UseCase
    participant GW as Gateway
    participant DB as Database

    C->>CT: GET /v1/usuarios/123
    CT->>CT: Valida Request
    CT->>UC: executar(BuscarUsuarioInput)
    UC->>GW: buscarPorId(123)
    GW->>DB: SELECT * FROM usuarios...
    DB-->>GW: ResultSet
    GW-->>UC: Usuario (Domain Entity)
    UC-->>CT: UsuarioOutput (DTO)
    CT->>CT: Mapper → Response
    CT-->>C: 200 OK + UsuarioResponse JSON
```

---

## Estrutura de Pacotes

```
com.grupo3.postech.jilocomjurubeba
├── domain/                    # 🟡 Camada de Domínio (sem Spring/JPA)
│   ├── entity/               # Entidades de negócio
│   ├── valueobject/          # Value Objects imutáveis
│   ├── gateway/              # Interfaces para mundo externo
│   └── exception/            # Exceções de domínio
│
├── application/              # 🟢 Camada de Aplicação
│   ├── usecase/              # Casos de uso
│   │   └── [entidade]/       # Agrupados por entidade
│   └── dto/                  # DTOs de entrada/saída
│       └── [entidade]/
│
├── interfaces/               # 🔵 Camada de Interfaces (REST)
│   └── rest/                 # Adaptadores REST
│       ├── dto/              # Request/Response
│       ├── mapper/           # Conversores REST
│       └── handler/          # Exception handlers
│
└── infrastructure/           # 🔴 Camada de Infraestrutura
    ├── persistence/          # Implementações de persistência
    │   ├── entity/           # Entidades JPA
    │   ├── repository/       # Repositórios Spring Data
    │   ├── gateway/          # Implementações dos Gateways
    │   └── mapper/           # Conversores de persistência
    └── config/               # Configurações Spring
```

---

## Regras de Dependência

### ⛔ Dependências Proibidas

| De                | Para           | Motivo                                          |
|-------------------|----------------|-------------------------------------------------|
| domain            | application    | Domain é o centro, não conhece casos de uso     |
| domain            | interfaces     | Domain não conhece HTTP                         |
| domain            | infrastructure | Domain não conhece banco de dados               |
| application       | interfaces     | Application não conhece HTTP                    |
| application       | infrastructure | Application não conhece implementações          |
| infrastructure    | interfaces     | Não devem se conhecer                           |

### ✅ Dependências Permitidas

| De                | Para        | Motivo                                             |
|-------------------|-------------|----------------------------------------------------|
| application       | domain      | Casos de uso orquestram entidades                  |
| interfaces        | application | Controllers chamam casos de uso                    |
| interfaces        | domain      | Pode usar exceções e tipos do domínio              |
| infrastructure    | application | Implementa interfaces, pode usar DTOs              |
| infrastructure    | domain      | Implementa Gateways do domínio                     |

---

## Validação com ArchUnit

O projeto usa ArchUnit para garantir que as regras de dependência são respeitadas:

```java
@Test
void domainNaoDeveDependerDeOutrasCamadas() {
    noClasses()
        .that().resideInAPackage("com.grupo3.postech.jilocomjurubeba.domain..")
        .should().dependOnClassesThat().resideInAnyPackage(
            "com.grupo3.postech.jilocomjurubeba.application..",
            "com.grupo3.postech.jilocomjurubeba.interfaces..",
            "com.grupo3.postech.jilocomjurubeba.infrastructure.."
        )
        .check(classes);
}
```

Execute os testes de arquitetura:
```bash
mvn test -Dtest=CleanArchitectureTest
```

---

## Referências

- [Clean Architecture - Robert C. Martin](https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html)
- [Hexagonal Architecture - Alistair Cockburn](https://alistair.cockburn.us/hexagonal-architecture/)
