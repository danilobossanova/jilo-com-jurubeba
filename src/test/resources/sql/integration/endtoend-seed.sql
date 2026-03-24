UPDATE usuario
SET nome = 'Dono Completo',
    cpf = '11111111111',
    email = 'dono@completo.com',
    telefone = '11999999999',
    senha_hash = '123'
WHERE id = 1;

UPDATE restaurante
SET nome = 'Restaurante Teste',
    endereco = 'Rua Principal',
    type_cozinha = 'ITALIANA',
    hora_abertura = '10:00',
    hora_fechamento = '22:00',
    dono_id = 1,
    ativo = TRUE
WHERE id = 1;

INSERT INTO cardapio (id, nome, descricao, preco, apenas_no_local, caminho_foto, restaurante_id, ativo)
VALUES (1, 'Entrada 1', 'Entrada clássica', 15.00, FALSE, '/entrada.jpg', 1, TRUE);

INSERT INTO cardapio (id, nome, descricao, preco, apenas_no_local, caminho_foto, restaurante_id, ativo)
VALUES (2, 'Prato Principal 1', 'Prato delicioso', 45.00, FALSE, '/prato.jpg', 1, TRUE);

INSERT INTO cardapio (id, nome, descricao, preco, apenas_no_local, caminho_foto, restaurante_id, ativo)
VALUES (3, 'Sobremesa 1', 'Sobremesa doce', 20.00, TRUE, '/sobremesa.jpg', 1, TRUE);
