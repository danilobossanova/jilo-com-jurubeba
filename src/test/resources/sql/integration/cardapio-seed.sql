INSERT INTO usuario (id, nome, cpf, email, telefone, tipo_usuario_id, ativo, senha_hash)
VALUES (2, 'Dono 2', '22222222222', 'dono2@teste.com', '11988888888', 3, TRUE, '456');

INSERT INTO restaurante (id, nome, endereco, type_cozinha, hora_abertura, hora_fechamento, dono_id, ativo)
VALUES (2, 'Restaurante B', 'Rua B', 'ITALIANA', '11:00', '23:00', 2, TRUE);

INSERT INTO cardapio (id, nome, descricao, preco, apenas_no_local, caminho_foto, restaurante_id, ativo)
VALUES (1, 'Pizza Margherita', 'Pizza tradicional', 45.00, FALSE, '/fotos/pizza.jpg', 1, TRUE);

INSERT INTO cardapio (id, nome, descricao, preco, apenas_no_local, caminho_foto, restaurante_id, ativo)
VALUES (2, 'Risoto de Cogumelo', 'Risoto gourmet', 55.00, TRUE, '/fotos/risoto.jpg', 1, TRUE);

INSERT INTO cardapio (id, nome, descricao, preco, apenas_no_local, caminho_foto, restaurante_id, ativo)
VALUES (3, 'Lasanha Bolonhesa', 'Lasanha clássica', 50.00, FALSE, '/fotos/lasanha.jpg', 2, TRUE);
