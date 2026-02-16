-- =====================================================================
-- V001: Criar tabela tipos_usuario
-- Armazena os tipos/papeis de usuario do sistema.
-- Substitui o enum tipo_usuario por tabela para permitir CRUD.
-- =====================================================================

CREATE TABLE tipos_usuario (
    id              BIGINT          AUTO_INCREMENT PRIMARY KEY,
    nome            VARCHAR(50)     NOT NULL,
    descricao       VARCHAR(255)    NOT NULL,
    ativo           BOOLEAN         NOT NULL DEFAULT TRUE,
    criado_em       DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP,
    atualizado_em   DATETIME        ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT uk_tipos_usuario_nome UNIQUE (nome),

    INDEX idx_tipos_usuario_nome (nome),
    INDEX idx_tipos_usuario_ativo (ativo)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Dados iniciais
INSERT INTO tipos_usuario (nome, descricao) VALUES
    ('MASTER', 'Administrador do sistema com acesso total'),
    ('DONO_RESTAURANTE', 'Proprietario de restaurante que gerencia cardapios'),
    ('CLIENTE', 'Cliente que consulta restaurantes e cardapios');
