TRUNCATE TABLE usuarios RESTART IDENTITY CASCADE;

INSERT INTO usuarios (id, username, password, rol, correo)
VALUES
(1,  'admin',      '$2y$12$BPhp39/2Kf9lqfs8pUcIl.xhSwiHeIVho7daxedqRGYcXtqbdGLtK', 'ADMIN',     'admin@duocuc.cl'),
(2,  'supervisor', '$2y$12$BPhp39/2Kf9lqfs8pUcIl.xhSwiHeIVho7daxedqRGYcXtqbdGLtK', 'ADMIN',     'supervisor@duocuc.cl'),
(3,  'vendedor1',  '$2y$12$BPhp39/2Kf9lqfs8pUcIl.xhSwiHeIVho7daxedqRGYcXtqbdGLtK', 'VENDEDOR',  'vendedor1@duocuc.cl'),
(4,  'vendedor2',  '$2y$12$BPhp39/2Kf9lqfs8pUcIl.xhSwiHeIVho7daxedqRGYcXtqbdGLtK', 'VENDEDOR',  'vendedor2@duocuc.cl'),
(5,  'bodega1',    '$2y$12$BPhp39/2Kf9lqfs8pUcIl.xhSwiHeIVho7daxedqRGYcXtqbdGLtK', 'ADMIN',     'bodega1@duocuc.cl'),
(6,  'cliente1',   '$2y$12$BPhp39/2Kf9lqfs8pUcIl.xhSwiHeIVho7daxedqRGYcXtqbdGLtK', 'USER',      'danaecatriina@gmail.com'),
(7,  'cliente2',   '$2y$12$BPhp39/2Kf9lqfs8pUcIl.xhSwiHeIVho7daxedqRGYcXtqbdGLtK', 'USER',      'cliente2@gmail.com'),
(8,  'cliente3',   '$2y$12$BPhp39/2Kf9lqfs8pUcIl.xhSwiHeIVho7daxedqRGYcXtqbdGLtK', 'USER',      'cliente3@hotmail.com'),
(9,  'cliente4',   '$2y$12$BPhp39/2Kf9lqfs8pUcIl.xhSwiHeIVho7daxedqRGYcXtqbdGLtK', 'USER',      'cliente4@gmail.com'),
(10, 'cliente5',   '$2y$12$BPhp39/2Kf9lqfs8pUcIl.xhSwiHeIVho7daxedqRGYcXtqbdGLtK', 'USER',      'cliente5@duocuc.cl'),
(11, 'cliente6',   '$2y$12$BPhp39/2Kf9lqfs8pUcIl.xhSwiHeIVho7daxedqRGYcXtqbdGLtK', 'USER',      'cliente6@hotmail.com'),
(12, 'cliente7',   '$2y$12$BPhp39/2Kf9lqfs8pUcIl.xhSwiHeIVho7daxedqRGYcXtqbdGLtK', 'USER',      'cliente7@gmail.com'),
(13, 'proveedor1', '$2y$12$BPhp39/2Kf9lqfs8pUcIl.xhSwiHeIVho7daxedqRGYcXtqbdGLtK', 'PROVEEDOR', 'proveedor1@gmail.com');

SELECT setval(pg_get_serial_sequence('usuarios', 'id'), (SELECT MAX(id) FROM usuarios));

SELECT * FROM usuarios;