UPDATE usuario
SET nome = 'Dono Silva',
    cpf = '11111111111',
    email = 'silva@teste.com',
    telefone = '11999999999',
    senha_hash = '123'
WHERE id = 1;

INSERT INTO usuario (id, nome, cpf, email, telefone, tipo_usuario_id, ativo, senha_hash)
VALUES (2, 'Dono Santos', '22222222222', 'santos@teste.com', '11988888888', 3, TRUE, '456');

INSERT INTO usuario (id, nome, cpf, email, telefone, tipo_usuario_id, ativo, senha_hash)
VALUES (3, 'Dono Oliveira', '33333333333', 'oliveira@teste.com', '11977777777', 3, TRUE, '789');

UPDATE restaurante
SET nome = 'Pizzaria do Silva',
    endereco = 'Rua A, 100',
    type_cozinha = 'ITALIANA',
    hora_abertura = '10:00',
    hora_fechamento = '23:00',
    dono_id = 1,
    ativo = TRUE
WHERE id = 1;

INSERT INTO restaurante (id, nome, endereco, type_cozinha, hora_abertura, hora_fechamento, dono_id, ativo)
VALUES (2, 'Churrascaria Santos', 'Rua B, 200', 'BRASILEIRA', '11:00', '00:00', 2, TRUE);

INSERT INTO restaurante (id, nome, endereco, type_cozinha, hora_abertura, hora_fechamento, dono_id, ativo)
VALUES (3, 'Sushi Bar Oliveira', 'Rua C, 300', 'JAPONESA', '12:00', '22:00', 3, TRUE);
