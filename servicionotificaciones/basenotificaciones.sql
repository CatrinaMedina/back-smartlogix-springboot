TRUNCATE TABLE notificaciones RESTART IDENTITY CASCADE;

INSERT INTO notificaciones (id, usuario_id, mensaje, tipo)
VALUES
(1,  1,  'Stock critico detectado: Teclado Mecanico Redragon tiene cantidad bajo el minimo.', 'STOCK'),
(2,  1,  'Stock critico detectado: Monitor Samsung 24 pulgadas requiere reposicion.', 'STOCK'),
(3,  1,  'Stock critico detectado: Impresora Epson EcoTank bajo stock minimo.', 'STOCK'),
(4,  1,  'Stock critico detectado: Base Notebook Ventilada requiere compra a proveedor.', 'STOCK'),
(5,  1,  'Stock critico detectado: Microfono Fifine USB bajo minimo permitido.', 'STOCK'),
(6,  6,  'Tu pedido #1 fue creado correctamente.', 'PEDIDO'),
(7,  7,  'Tu pedido #2 fue creado correctamente.', 'PEDIDO'),
(8,  8,  'Tu pedido #3 fue creado correctamente.', 'PEDIDO'),
(9,  9,  'Tu pedido #4 fue creado correctamente.', 'PEDIDO'),
(10, 10, 'Tu pedido #5 fue creado correctamente.', 'PEDIDO'),
(11, 6,  'Tu envio asociado al pedido #1 se encuentra pendiente.', 'ENVIO'),
(12, 7,  'Tu envio asociado al pedido #2 se encuentra en camino.', 'ENVIO'),
(13, 8,  'Tu envio asociado al pedido #3 esta siendo preparado.', 'ENVIO'),
(14, 9,  'Tu envio asociado al pedido #4 fue entregado correctamente.', 'ENVIO'),
(15, 10, 'Tu envio asociado al pedido #5 se encuentra en camino.', 'ENVIO'),
(16, 10, 'Tu pedido #10 fue creado correctamente.', 'PEDIDO'),
(17, 10, 'Tu envio asociado al pedido #10 fue entregado correctamente.', 'ENVIO');

SELECT setval(pg_get_serial_sequence('notificaciones', 'id'), (SELECT MAX(id) FROM notificaciones));

SELECT * FROM notificaciones;