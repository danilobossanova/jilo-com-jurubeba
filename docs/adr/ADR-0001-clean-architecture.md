# ADR-0001: Adoção de Clean Architecture

**Data:** 2026-01-24

**Status:** Aguardando Aceite

**Decisor(es):** Grupo 3 - Postech

**Autor:** Danilo Fernando

---

## Contexto

O projeto jilo-com-jurubeba é um sistema de gestão de restaurantes que precisa:

1. Evoluir rapidamente com novas funcionalidades
2. Suportar múltiplos bancos de dados (PostgreSQL, MySQL, MongoDB)
3. Manter alta testabilidade (meta: 80% cobertura)
4. Permitir que o time trabalhe em paralelo sem conflitos
5. Facilitar onboarding de novos desenvolvedores

O sistema terá complexidade de negócio baixa a moderada, com regras de gestão de restaurantes, gestão de cardápios, e múltiplos tipos de usuários.

---

## Decisão

Adotamos **Clean Architecture** como padrão arquitetural do projeto, com as seguintes definições específicas:

### 1. Estrutura de Camadas

| Camada         | Pacote                                        | Responsabilidade                          |
|----------------|-----------------------------------------------|-------------------------------------------|
| Domain         | `com.grupo3.postech.jilocomjurubeba.domain`   | Entidades, Value Objects, Gateways        |
| Application    | `com.grupo3.postech.jilocomjurubeba.application` | Casos de uso, DTOs de entrada/saída    |
| Interfaces     | `com.grupo3.postech.jilocomjurubeba.interfaces`  | Controllers REST, handlers de erro     |
| Infrastructure | `com.grupo3.postech.jilocomjurubeba.infrastructure` | Persistência, configurações, integrações |

### 2. Nomenclatura de Classes

| Componente          | Padrão de Nome                      | Exemplo                          |
|---------------------|-------------------------------------|----------------------------------|
| Entidade            | `[Nome]`                            | `Usuario`, `Restaurante`         |
| Value Object        | `[Nome]`                            | `Email`, `Cpf`, `Preco`          |
| Gateway (interface) | `[Entidade]Gateway`                 | `UsuarioGateway`                 |
| Gateway (impl)      | `[Entidade]Gateway[Tecnologia]`     | `UsuarioGatewayJpa`              |
| UseCase             | `[Acao][Entidade]UseCase`           | `CriarUsuarioUseCase`            |
| DTO Input           | `[Acao][Entidade]Input`             | `CriarUsuarioInput`              |
| DTO Output          | `[Entidade]Output`                  | `UsuarioOutput`                  |
| Controller          | `[Entidade]Controller`              | `UsuarioController`              |
| Request DTO         | `[Acao][Entidade]Request`           | `CriarUsuarioRequest`            |
| Response DTO        | `[Entidade]Response`                | `UsuarioResponse`                |
| JPA Entity          | `[Entidade]JpaEntity`               | `UsuarioJpaEntity`               |
| Repository          | `[Entidade]Repository`              | `UsuarioRepository`              |
| Mapper REST         | `[Entidade]RestMapper`              | `UsuarioRestMapper`              |
| Mapper Persistence  | `[Entidade]PersistenceMapper`       | `UsuarioPersistenceMapper`       |
| Exceção             | `[Tipo]Exception`                   | `ValidacaoException`             |

### 3. Estratégia de DTOs e Mappers

**Decisão:** Usar Records Java para DTOs e MapStruct para mapeamento.

**Razões:**
- Records são imutáveis por padrão
- Menos boilerplate (sem getters/setters/equals/hashCode manuais)
- MapStruct gera código em tempo de compilação (sem reflexão em runtime)

### 4. Estratégia de Exceptions e ProblemDetail

**Hierarquia de Exceções:**

```
DominioException (abstract)
├── ValidacaoException        → HTTP 400 Bad Request
├── EntidadeNaoEncontradaException → HTTP 404 Not Found
└── RegraDeNegocioException   → HTTP 422 Unprocessable Entity
```

**ProblemDetail (RFC 7807):** Todas as respostas de erro seguem o padrão RFC 7807.

### 5. Estratégia de Testes por Camada

| Camada         | Tipo              | Cobertura Meta | Ferramentas                        |
|----------------|-------------------|----------------|------------------------------------|
| Domain         | Unitário          | 90%            | JUnit 5, AssertJ                   |
| Application    | Unitário          | 85%            | JUnit 5, Mockito                   |
| Interfaces     | Integração        | 80%            | MockMvc, @WebMvcTest               |
| Infrastructure | Integração        | 75%            | Testcontainers, @DataJpaTest       |

### 6. Registro de UseCases como Beans

**Decisão:** Não usar `@Component` nos UseCases. Registrar manualmente em `UseCaseConfig`.

**Razões:**
- Mantém camada application livre de anotações Spring
- Centraliza criação de beans
- Facilita testes unitários sem Spring

---

## Alternativas Consideradas

### 1. Arquitetura em Camadas Tradicional (Controller-Service-Repository)

**Prós:**
- Mais simples de implementar inicialmente
- Mais familiar para maioria dos desenvolvedores

**Contras:**
- Regras de negócio ficam acopladas a Spring
- Difícil trocar banco de dados
- Difícil testar sem Spring

**Por que rejeitamos:** Não atende requisitos de testabilidade e flexibilidade de banco de dados.

### 2. CQRS (Command Query Responsibility Segregation)

**Prós:**
- Separação de leitura e escrita
- Escalabilidade de leitura

**Contras:**
- Complexidade adicional
- Overkill para escala atual

**Por que rejeitamos:** Podemos adotar posteriormente se necessário.

---

## Consequências

### Positivas

1. **Testabilidade:** Domain e Application 100% testáveis sem Spring
2. **Flexibilidade:** Fácil trocar PostgreSQL por MongoDB
3. **Manutenibilidade:** Código organizado e previsível
4. **Qualidade:** ArchUnit previne violações de arquitetura

### Negativas

1. **Boilerplate:** Mais classes e mapeamentos
2. **Curva de aprendizado:** Time precisa entender Clean Architecture

### Mitigações

| Consequência         | Mitigação                                           |
|----------------------|-----------------------------------------------------|
| Boilerplate          | MapStruct gera código automaticamente               |
| Curva de aprendizado | Documentação clara + pair programming               |

---

## Referências

- [Clean Architecture - Robert C. Martin](https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html)
- [ADR - Architecture Decision Records](https://adr.github.io/)
- [RFC 7807 - Problem Details for HTTP APIs](https://datatracker.ietf.org/doc/html/rfc7807)
