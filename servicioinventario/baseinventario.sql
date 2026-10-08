TRUNCATE TABLE producto RESTART IDENTITY CASCADE;
TRUNCATE TABLE bodega RESTART IDENTITY CASCADE;
TRUNCATE TABLE proveedor RESTART IDENTITY CASCADE;

INSERT INTO bodega (id, nombre, direccion, tipo)
VALUES
(1, 'Bodega Central Santiago', 'Av. Vicuña Mackenna 1200, Santiago', 'BODEGA'),
(2, 'Sucursal Maipú', 'Av. Pajaritos 4500, Maipú', 'SUCURSAL'),
(3, 'Sucursal Providencia', 'Av. Providencia 1800, Providencia', 'SUCURSAL'),
(4, 'Tienda Online SmartLogix', 'Operación eCommerce Nacional', 'TIENDA'),
(5, 'Bodega Norte', 'Camino Industrial 300, Quilicura', 'BODEGA'),
(6, 'Bodega Sur', 'Av. Departamental 2200, San Miguel', 'BODEGA');

INSERT INTO proveedor (id, nombre, contacto)
VALUES
(1, 'TechImport Chile SPA', 'contacto@techimport.cl'),
(2, 'Distribuidora Logística Andes', 'ventas@logisticaandes.cl'),
(3, 'Proveedor Digital Center', 'soporte@digitalcenter.cl'),
(4, 'ElectroMarket Mayorista', 'mayorista@electromarket.cl'),
(5, 'Chile Componentes SPA', 'contacto@chilecomponentes.cl'),
(6, 'SmartSupply LATAM', 'ventas@smartsupply.cl');

INSERT INTO producto (id, nombre, descripcion, cantidad, precio, tipo_stock, stock_minimo, bodega_id, proveedor_id)
VALUES
(1, 'Notebook Lenovo IdeaPad', 'Notebook para venta online, 8GB RAM, SSD 256GB', 18, 459990, 'VENTA', 5, 1, 1),
(2, 'Notebook HP Pavilion', 'Notebook HP 15 pulgadas, 16GB RAM, SSD 512GB', 9, 629990, 'VENTA', 4, 1, 1),
(3, 'Notebook Asus VivoBook', 'Notebook liviano para estudiantes y oficina', 6, 519990, 'VENTA', 4, 2, 3),
(4, 'Mouse Logitech M185', 'Mouse inalámbrico básico USB', 45, 9990, 'VENTA', 12, 1, 4),
(5, 'Mouse Gamer Redragon', 'Mouse gamer RGB 7200 DPI', 22, 18990, 'VENTA', 8, 2, 4),
(6, 'Teclado Mecánico Redragon', 'Teclado mecánico RGB switches rojos', 4, 39990, 'CRITICO', 6, 2, 5),
(7, 'Teclado Logitech K120', 'Teclado USB estándar para oficina', 28, 10990, 'VENTA', 10, 1, 4),
(8, 'Monitor Samsung 24"', 'Monitor LED Full HD 24 pulgadas', 3, 119990, 'CRITICO', 5, 1, 2),
(9, 'Monitor LG 27"', 'Monitor IPS Full HD 27 pulgadas', 7, 159990, 'VENTA', 4, 3, 2),
(10, 'Audífonos JBL Tune', 'Audífonos bluetooth con micrófono', 16, 34990, 'VENTA', 6, 4, 3),
(11, 'Audífonos Gamer HyperX', 'Audífonos gamer con micrófono y sonido envolvente', 5, 69990, 'CRITICO', 6, 4, 3),
(12, 'Webcam Logitech C270', 'Cámara web HD para videollamadas', 12, 29990, 'VENTA', 5, 5, 4),
(13, 'Impresora Epson EcoTank', 'Impresora multifuncional con sistema continuo', 4, 189990, 'CRITICO', 5, 5, 6),
(14, 'Disco SSD Kingston 480GB', 'Unidad de estado sólido SATA 480GB', 24, 42990, 'VENTA', 8, 6, 5),
(15, 'Disco SSD Kingston 1TB', 'Unidad de estado sólido SATA 1TB', 8, 74990, 'VENTA', 5, 6, 5),
(16, 'Memoria RAM Kingston 8GB', 'Memoria RAM DDR4 8GB', 30, 27990, 'VENTA', 10, 6, 5),
(17, 'Memoria RAM Kingston 16GB', 'Memoria RAM DDR4 16GB', 6, 49990, 'CRITICO', 8, 6, 5),
(18, 'Router TP-Link Archer', 'Router WiFi doble banda', 14, 45990, 'VENTA', 5, 3, 6),
(19, 'Cable HDMI 2 metros', 'Cable HDMI alta velocidad', 60, 4990, 'VENTA', 20, 2, 2),
(20, 'Pendrive Sandisk 64GB', 'Pendrive USB 3.0 64GB', 35, 8990, 'VENTA', 15, 4, 1),
(21, 'Pendrive Kingston 128GB', 'Pendrive USB 3.1 128GB', 11, 14990, 'VENTA', 6, 4, 1),
(22, 'Base Notebook Ventilada', 'Base con ventiladores para notebook', 2, 19990, 'CRITICO', 5, 2, 6),
(23, 'Cargador Universal Notebook', 'Cargador universal 90W para notebook', 5, 24990, 'CRITICO', 6, 1, 6),
(24, 'Tablet Samsung Galaxy Tab', 'Tablet Android 10 pulgadas', 10, 179990, 'VENTA', 4, 3, 1),
(25, 'Smartwatch Xiaomi Band', 'Pulsera inteligente con monitor de actividad', 20, 39990, 'VENTA', 7, 4, 3),
(26, 'Silla Gamer Cougar', 'Silla gamer ergonómica color negro', 3, 149990, 'CRITICO', 4, 5, 2),
(27, 'Hub USB-C 6 en 1', 'Adaptador multipuerto USB-C HDMI y USB', 13, 29990, 'VENTA', 5, 6, 5),
(28, 'Parlante Bluetooth Sony', 'Parlante portátil resistente al agua', 9, 59990, 'VENTA', 4, 4, 3),
(29, 'Micrófono Fifine USB', 'Micrófono para streaming y clases online', 4, 44990, 'CRITICO', 5, 3, 4),
(30, 'Adaptador WiFi USB', 'Adaptador inalámbrico USB 600Mbps', 17, 12990, 'VENTA', 7, 2, 6);

SELECT setval(pg_get_serial_sequence('bodega', 'id'), (SELECT MAX(id) FROM bodega));
SELECT setval(pg_get_serial_sequence('proveedor', 'id'), (SELECT MAX(id) FROM proveedor));
SELECT setval(pg_get_serial_sequence('producto', 'id'), (SELECT MAX(id) FROM producto));

SELECT * FROM bodega;
SELECT * FROM proveedor;
SELECT * FROM producto;