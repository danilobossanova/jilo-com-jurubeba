# Guia de Contribuição - Projeto jilo-com-jurubeba

> @author Danilo Fernando

Obrigado por contribuir com o projeto! Este documento descreve os padrões e processos que seguimos.

---

## Padrão de Branches

### Nomenclatura

```
<tipo>/<numero-issue>-<descricao-curta>
```

### Tipos de Branch

| Tipo      | Descrição                        | Exemplo                              |
|-----------|----------------------------------|--------------------------------------|
| `feature` | Nova funcionalidade              | `feature/123-cadastro-usuario`       |
| `fix`     | Correção de bug                  | `fix/456-validacao-email`            |
| `refactor`| Refatoração                      | `refactor/789-extrair-usecase`       |
| `docs`    | Apenas documentação              | `docs/101-atualizar-readme`          |
| `test`    | Apenas testes                    | `test/102-testes-gateway`            |
| `chore`   | Tarefas de infraestrutura        | `chore/103-atualizar-dependencias`   |

---

## Padrão de Commits

### Formato (Conventional Commits)

```
<tipo>(<escopo>): <descrição>

[corpo opcional]

[rodapé opcional]
```

### Tipos de Commit

| Tipo       | Descrição                                    |
|------------|----------------------------------------------|
| `feat`     | Nova funcionalidade                          |
| `fix`      | Correção de bug                              |
| `docs`     | Documentação apenas                          |
| `style`    | Formatação (não afeta código)                |
| `refactor` | Refatoração                                  |
| `test`     | Adição ou correção de testes                 |
| `chore`    | Tarefas de build, CI, dependências           |

### Exemplos

```bash
feat(domain): adicionar entidade Usuario com validações
fix(application): corrigir mapeamento de CPF no CriarUsuarioUseCase
docs: adicionar ADR para escolha de banco de dados
test(application): adicionar testes para BuscarUsuarioUseCase
```

---

## Processo de Pull Request

### Antes de Abrir o PR

1. **Sincronizar com master:**
   ```bash
   git fetch origin
   git rebase origin/master
   ```

2. **Rodar testes localmente:**
   ```bash
   mvn clean test
   ```

3. **Verificar arquitetura:**
   ```bash
   mvn test -Dtest=CleanArchitectureTest
   ```

### Template de PR

```markdown
## Descrição
[Descreva o que foi implementado/corrigido]

## Issue Relacionada
Closes #[número]

## Tipo de Mudança
- [ ] Nova funcionalidade (feature)
- [ ] Correção de bug (fix)
- [ ] Refatoração
- [ ] Documentação
- [ ] Testes

## Checklist
- [ ] Testes unitários adicionados/atualizados
- [ ] Documentação atualizada (se aplicável)
- [ ] ArchUnit passa sem erros
- [ ] Build passa localmente (`mvn clean package`)
```

---

## Code Review

### O que Verificar no Review

#### Arquitetura
- [ ] Segue Clean Architecture?
- [ ] Dependências entre camadas corretas?
- [ ] Nomenclatura segue convenções?

#### Qualidade de Código
- [ ] Código legível e bem organizado?
- [ ] Sem duplicação de código?
- [ ] Tratamento de erros adequado?

#### Testes
- [ ] Testes unitários existem?
- [ ] Cenários de erro testados?
- [ ] Cobertura adequada (>80%)?

---

## Definition of Done

Uma tarefa só é considerada **DONE** quando:

### Código
- [ ] Implementação completa conforme requisitos
- [ ] Código segue convenções do projeto
- [ ] Sem warnings de compilação

### Testes
- [ ] Testes unitários escritos e passando
- [ ] Cobertura de código >= 80%
- [ ] ArchUnit passa

### Documentação
- [ ] Javadoc em classes/métodos públicos
- [ ] README atualizado (se necessário)

### Processo
- [ ] PR aprovado por pelo menos 1 pessoa
- [ ] CI passou (build + testes)
- [ ] Branch mergeada em master

---

## Setup do Ambiente

### Pré-requisitos

- Java 21+ (recomendado: Eclipse Temurin)
- Maven 3.9+
- Docker e Docker Compose
- Git

### Clone e Setup

```bash
# Clone do repositório
git clone https://github.com/grupo3-postech/jilo-com-jurubeba.git
cd jilo-com-jurubeba

# Build inicial
mvn clean install

# Subir banco de dados (PostgreSQL)
docker-compose -f docker-compose.dev.yml up -d

# Rodar aplicação
mvn spring-boot:run

# Verificar health
curl http://localhost:8080/v1/health

# Rodar testes
mvn test
```

---

## Dúvidas?

- Abra uma issue com a tag `question`
- Consulte a documentação em `/docs`
