# Modelo de Dados - jilo-com-jurubeba

## Arquivos

| Arquivo       | Formato     | Uso                                                    |
|---------------|-------------|--------------------------------------------------------|
| `schema.sql`  | SQL DDL     | PostgreSQL 16 - importar em DBeaver, pgAdmin, etc.     |
| `schema.dbml` | DBML        | Colar em [dbdiagram.io](https://dbdiagram.io) para gerar MER/DER |

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

### Fase 2 (Implementacao atual)

| Tabela                    | Descricao                                    |
|---------------------------|----------------------------------------------|
| `usuarios`                | Usuarios do sistema (DONO_RESTAURANTE, CLIENTE) |
| `enderecos`               | Enderecos dos restaurantes                   |
| `restaurantes`            | Restaurantes cadastrados                     |
| `horarios_funcionamento`  | Horarios de abertura/fechamento por dia      |
| `itens_cardapio`          | Itens do cardapio de cada restaurante        |

### Fase futura (ja modelada)

| Tabela       | Descricao                                        |
|--------------|--------------------------------------------------|
| `avaliacoes` | Avaliacoes dos clientes (1-5 estrelas)           |
| `reservas`   | Reservas de mesas nos restaurantes               |

## Relacionamentos

```
usuarios (1) ----< (N) restaurantes         [dono_id]
enderecos (1) ---- (1) restaurantes         [endereco_id]
restaurantes (1) --< (N) horarios_func      [restaurante_id]
restaurantes (1) --< (N) itens_cardapio     [restaurante_id]
restaurantes (1) --< (N) avaliacoes         [restaurante_id]
usuarios (1) ------< (N) avaliacoes         [cliente_id]
restaurantes (1) --< (N) reservas           [restaurante_id]
usuarios (1) ------< (N) reservas           [cliente_id]
```
