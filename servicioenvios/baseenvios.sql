TRUNCATE TABLE envios RESTART IDENTITY CASCADE;

INSERT INTO envios (id, pedido_id, direccion, estado, transportista, tipo_transportista, codigo_seguimiento, dias_estimados, fecha_estimada_entrega)
VALUES
(1, 1, 'Av. Siempre Viva 123, Maipú', 'PENDIENTE', 'Chilexpress', 'EMPRESA_EXTERNA', 'TRK-1-SLX', 3, CURRENT_DATE + INTERVAL '3 days'),
(2, 2, 'Los Aromos 456, Santiago', 'EN_CAMINO', 'Starken', 'EMPRESA_EXTERNA', 'TRK-2-SLX', 2, CURRENT_DATE + INTERVAL '2 days'),
(3, 3, 'Pasaje Las Flores 789, Providencia', 'PENDIENTE', 'Blue Express', 'EMPRESA_EXTERNA', 'TRK-3-SLX', 4, CURRENT_DATE + INTERVAL '4 days'),
(4, 4, 'Av. Central 1010, San Miguel', 'ENTREGADO', 'Chilexpress', 'EMPRESA_EXTERNA', 'TRK-4-SLX', 3, CURRENT_DATE + INTERVAL '3 days'),
(5, 5, 'Camino El Alba 2020, Las Condes', 'EN_CAMINO', 'Starken', 'EMPRESA_EXTERNA', 'TRK-5-SLX', 2, CURRENT_DATE + INTERVAL '2 days'),
(6, 6, 'Av. Pajaritos 3333, Maipú', 'PENDIENTE', 'Chilexpress', 'EMPRESA_EXTERNA', 'TRK-6-SLX', 3, CURRENT_DATE + INTERVAL '3 days'),
(7, 7, 'Av. Grecia 4444, Ñuñoa', 'CANCELADO', 'Blue Express', 'EMPRESA_EXTERNA', 'TRK-7-SLX', 5, CURRENT_DATE + INTERVAL '5 days'),
(8, 8, 'San Diego 555, Santiago', 'EN_CAMINO', 'Starken', 'EMPRESA_EXTERNA', 'TRK-8-SLX', 2, CURRENT_DATE + INTERVAL '2 days'),
(9, 9, 'Av. Matta 666, Santiago', 'PENDIENTE', 'Chilexpress', 'EMPRESA_EXTERNA', 'TRK-9-SLX', 3, CURRENT_DATE + INTERVAL '3 days'),
(10, 10, 'Las Encinas 777, La Florida', 'ENTREGADO', 'Blue Express', 'EMPRESA_EXTERNA', 'TRK-10-SLX', 4, CURRENT_DATE + INTERVAL '4 days');

SELECT setval(pg_get_serial_sequence('envios', 'id'), (SELECT MAX(id) FROM envios));

SELECT * FROM envios;