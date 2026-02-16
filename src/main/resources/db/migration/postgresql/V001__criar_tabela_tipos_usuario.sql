-- =====================================================================
-- V001: Criar tabela tipos_usuario
-- Armazena os tipos/papeis de usuario do sistema.
-- Substitui o enum tipo_usuario por tabela para permitir CRUD.
-- =====================================================================

CREATE TABLE tipos_usuario (
    id              BIGSERIAL       PRIMARY KEY,
    nome            VARCHAR(50)     NOT NULL,
    descricao       VARCHAR(255)    NOT NULL,
    ativo           BOOLEAN         NOT NULL DEFAULT TRUE,
    criado_em       TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    atualizado_em   TIMESTAMP,

    CONSTRAINT uk_tipos_usuario_nome UNIQUE (nome)
);

CREATE INDEX idx_tipos_usuario_nome ON tipos_usuario (nome);
CREATE INDEX idx_tipos_usuario_ativo ON tipos_usuario (ativo);

COMMENT ON TABLE tipos_usuario IS 'Tipos/papeis de usuario no sistema';
COMMENT ON COLUMN tipos_usuario.nome IS 'Nome unico do tipo (MASTER, DONO_RESTAURANTE, CLIENTE)';
COMMENT ON COLUMN tipos_usuario.descricao IS 'Descricao legivel do tipo de usuario';
COMMENT ON COLUMN tipos_usuario.ativo IS 'Soft delete - FALSE significa tipo desativado';

-- Dados iniciais
INSERT INTO tipos_usuario (nome, descricao) VALUES
    ('MASTER', 'Administrador do sistema com acesso total'),
    ('DONO_RESTAURANTE', 'Proprietario de restaurante que gerencia cardapios'),
    ('CLIENTE', 'Cliente que consulta restaurantes e cardapios');
