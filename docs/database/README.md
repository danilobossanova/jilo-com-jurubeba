# Modelo de Dados - jilo-com-jurubeba

## Fontes de Verdade

| Fonte | Proposito |
|-------|-----------|
| **Migrations Flyway** (`src/main/resources/db/migration/`) | DDL executavel — fonte da verdade para o banco |
| `schema.sql` | Referencia completa PostgreSQL — para documentacao e import em ferramentas |
| `schema.dbml` | Modelo visual — para gerar MER/DER no [dbdiagram.io](https://dbdiagram.io) |

> **Regra:** Ao criar/alterar uma tabela, atualizar os 3 artefatos: migration + schema.sql + schema.dbml.

## Arquivos

| Arquivo       | Formato     | Uso                                                    |
|---------------|-------------|--------------------------------------------------------|
| `schema.sql`  | SQL DDL     | PostgreSQL 16 - importar em DBeaver, pgAdmin, etc.     |
| `schema.dbml` | DBML        | Colar em [dbdiagram.io](https://dbdiagram.io) para gerar MER/DER |

## Migrations Flyway

As migrations estao em `src/main/resources/db/migration/` separadas por dialeto:

```
db/migration/
├── postgresql/
│   └── V001__criar_tabela_tipos_usuario.sql
└── mysql/
    └── V001__criar_tabela_tipos_usuario.sql
```

**Status atual:** Flyway desabilitado (`spring.flyway.enabled=false`). O projeto usa `ddl-auto=update`.
Quando Flyway for habilitado, `ddl-auto` sera mudado para `validate`.

### Convencoes de Migrations

- Nome: `V{NNN}__descricao_em_snake_case.sql`
- Sempre criar para AMBOS os dialetos (PostgreSQL + MySQL)
- Campos padrao em toda tabela: `id`, `ativo`, `criado_em`, `atualizado_em`

## Como gerar o diagrama MER/DER

### Opcao 1: dbdiagram.io (recomendado)

1. Acesse [dbdiagram.io](https://dbdiagram.io)
2. Cole o conteudo de `schema.dbml` no editor da esquerda
3. O diagrama sera gerado automaticamente
4. Exporte como PNG, PDF ou SVG

### Opcao 2: DBeaver

1. Abra o DBeaver e conecte a um banco PostgreSQL
2. Execute o script `schema.sql`
3. Clique com botao direito no banco > "View Diagram"

### Opcao 3: pgAdmin

1. Abra o pgAdmin e conecte ao PostgreSQL
2. Execute o script `schema.sql` no Query Tool
3. Use a ferramenta ERD do pgAdmin 4

## Tabelas

### Implementadas

| Tabela           | Migration | Descricao                                          |
|------------------|-----------|---------------------------------------------------|
| `tipos_usuario`  | V001      | Tipos/papeis de usuario (MASTER, DONO_RESTAURANTE, CLIENTE) |

### Fase 2 (planejadas)

| Tabela                    | Descricao                                    |
|---------------------------|----------------------------------------------|
| `usuarios`                | Usuarios do sistema (FK → tipos_usuario)     |
| `enderecos`               | Enderecos dos restaurantes                   |
| `restaurantes`            | Restaurantes cadastrados (FK → usuarios, enderecos) |
| `horarios_funcionamento`  | Horarios de abertura/fechamento por dia      |
| `itens_cardapio`          | Itens do cardapio de cada restaurante        |

### Fase futura (ja modelada)

| Tabela       | Descricao                                        |
|--------------|--------------------------------------------------|
| `avaliacoes` | Avaliacoes dos clientes (1-5 estrelas)           |
| `reservas`   | Reservas de mesas nos restaurantes               |

## Relacionamentos

```
tipos_usuario (1) --< (N) usuarios            [tipo_usuario_id]
usuarios (1) ------< (N) restaurantes         [dono_id]
enderecos (1) ----- (1) restaurantes          [endereco_id]
restaurantes (1) --< (N) horarios_func        [restaurante_id]
restaurantes (1) --< (N) itens_cardapio       [restaurante_id]
restaurantes (1) --< (N) avaliacoes           [restaurante_id]
usuarios (1) ------< (N) avaliacoes           [cliente_id]
restaurantes (1) --< (N) reservas             [restaurante_id]
usuarios (1) ------< (N) reservas             [cliente_id]
```

## Decisoes de Modelagem

| Decisao | Justificativa |
|---------|---------------|
| `tipo_usuario` como **tabela** (nao enum) | Permite CRUD, extensibilidade sem ALTER TYPE, e dados semeados via DataSeeder |
| `tipo_cozinha` como **enum** | Conjunto fixo, raramente muda, performance melhor em filtros |
| `atualizado_em` **nullable** | NULL indica "nunca atualizado" — mais semantico que DEFAULT CURRENT_TIMESTAMP |
| Soft delete via `ativo` | Nunca apagar dados fisicamente — auditoria e recuperacao |
| Enderecos em tabela separada | Reutilizavel para usuarios (futuro) e evita duplicacao |
