INSERT INTO tipo_usuario (nome, descricao, ativo)
VALUES ('MASTER', 'Administrador master', TRUE);

INSERT INTO tipo_usuario (nome, descricao, ativo)
VALUES ('CLIENTE', 'Cliente do sistema', TRUE);

INSERT INTO tipo_usuario (nome, descricao, ativo)
VALUES ('DONO_RESTAURANTE', 'Proprietario', TRUE);

-- DONO DO RESTAURANTE
INSERT INTO usuario (id, nome, cpf, email, telefone, tipo_usuario_id, ativo, senha_hash)
VALUES (1, 'Dono Teste', '12345678909', 'dono@teste.com', '11999999999', 3, TRUE, '123');

-- RESTAURANTE
INSERT INTO restaurante (id, nome, endereco, type_cozinha, hora_abertura, hora_fechamento, dono_id, ativo)
VALUES (1, 'Restaurante XPTO', 'Rua A', 'BRASILEIRA', '10:00', '22:00', 1, TRUE);

