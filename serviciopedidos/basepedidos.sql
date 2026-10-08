TRUNCATE TABLE boletas RESTART IDENTITY CASCADE;
TRUNCATE TABLE pedidos RESTART IDENTITY CASCADE;

INSERT INTO pedidos (id, username, producto_id, cantidad, estado, tipo, direccion_envio)
VALUES
(1,  'cliente1', 1,  1, 'CREADO', 'NORMAL',  'Av. Siempre Viva 123, Maipú'),
(2,  'cliente2', 4,  2, 'CREADO', 'EXPRESS', 'Los Aromos 456, Santiago'),
(3,  'cliente3', 8,  1, 'CREADO', 'NORMAL',  'Pasaje Las Flores 789, Providencia'),
(4,  'cliente4', 14, 2, 'CREADO', 'NORMAL',  'Av. Central 1010, San Miguel'),
(5,  'cliente5', 20, 3, 'CREADO', 'EXPRESS', 'Camino El Alba 2020, Las Condes'),
(6,  'cliente1', 6,  1, 'CREADO', 'EXPRESS', 'Av. Pajaritos 3333, Maipú'),
(7,  'cliente2', 10, 1, 'CREADO', 'NORMAL',  'Av. Grecia 4444, Ñuñoa'),
(8,  'cliente3', 15, 1, 'CREADO', 'NORMAL',  'San Diego 555, Santiago'),
(9,  'cliente4', 25, 2, 'CREADO', 'EXPRESS', 'Av. Matta 666, Santiago'),
(10, 'cliente5', 27, 1, 'CREADO', 'NORMAL',  'Las Encinas 777, La Florida');

INSERT INTO boletas (id, pedido_id, username, precio_neto, iva, precio_total, fecha_emision)
VALUES
(1,  1,  'cliente1', 459990, 87398,  547388, NOW()),
(2,  2,  'cliente2', 19980,  3796,   23776,  NOW()),
(3,  3,  'cliente3', 119990, 22798,  142788, NOW()),
(4,  4,  'cliente4', 85980,  16336,  102316, NOW()),
(5,  5,  'cliente5', 26970,  5124,   32094,  NOW()),
(6,  6,  'cliente1', 39990,  7598,   47588,  NOW()),
(7,  7,  'cliente2', 34990,  6648,   41638,  NOW()),
(8,  8,  'cliente3', 74990,  14248,  89238,  NOW()),
(9,  9,  'cliente4', 79980,  15196,  95176,  NOW()),
(10, 10, 'cliente5', 29990,  5698,   35688,  NOW());

SELECT setval(pg_get_serial_sequence('pedidos', 'id'), (SELECT MAX(id) FROM pedidos));
SELECT setval(pg_get_serial_sequence('boletas', 'id'), (SELECT MAX(id) FROM boletas));

SELECT * FROM pedidos;
SELECT * FROM boletas;