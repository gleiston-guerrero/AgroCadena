-- db/03-datos-demo.sql
-- Datos de demostracion: el contenedor CNT-007 del libro y sus dispositivos.
-- Ejecutar despues de 02-funciones-y-triggers.sql.
INSERT INTO contenedor (codigo, producto, temp_min, temp_max) VALUES
	('CNT-007', 'banano', -1, 8),
	('CNT-012', 'cacao',  12, 20)
ON CONFLICT (codigo) DO NOTHING;

INSERT INTO dispositivo (dev_eui, modelo, activo, contenedor_id)
SELECT v.dev_eui, v.modelo, TRUE, c.id
FROM (VALUES
	('24E124136B316001', 'EM300-TH', 'CNT-007'),
	('24E124141C147002', 'WS301',    'CNT-007'),
	('24E124136B316003', 'EM300-TH', 'CNT-012')
) AS v(dev_eui, modelo, cont)
JOIN contenedor c ON c.codigo = v.cont
ON CONFLICT (dev_eui) DO NOTHING;
