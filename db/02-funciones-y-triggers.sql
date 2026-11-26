-- 1) Hipertabla TimescaleDB sobre la tabla lectura (creada por Hibernate).
CREATE EXTENSION IF NOT EXISTS timescaledb;
-- TimescaleDB exige que toda clave unica incluya la columna de particion:
-- la clave primaria (id) creada por Hibernate se amplia a (id, instante).
ALTER TABLE lectura DROP CONSTRAINT IF EXISTS lectura_pkey;
ALTER TABLE lectura ADD PRIMARY KEY (id, instante);
SELECT create_hypertable('lectura', 'instante', if_not_exists => TRUE, migrate_data => TRUE);

-- 2) Funcion que evalua si hay excursion termica sostenida en N minutos.
CREATE OR REPLACE FUNCTION fn_excursion_sostenida(
                            p_dispositivo BIGINT,
                            p_temp_min    NUMERIC,
                            p_temp_max    NUMERIC,
                            p_minutos     INT) 
                            RETURNS BOOLEAN AS $$
DECLARE n_fuera INT;
BEGIN
	SELECT COUNT(*) INTO n_fuera
	FROM lectura
	WHERE dispositivo_id = p_dispositivo AND
    instante > NOW() - (p_minutos || ' minutes')::INTERVAL
	AND (temperatura < p_temp_min OR 
        temperatura > p_temp_max);
        
    -- al menos una por intervalo
	RETURN n_fuera >= (p_minutos / 5);
END;
$$ LANGUAGE plpgsql;
-- 3) Trigger: al insertar una lectura fuera de rango,
--    generar alerta si la condicion se sostiene 10 min
--    (atiende RF-03 a nivel de base).

CREATE OR REPLACE FUNCTION trg_alerta_excursion() RETURNS TRIGGER AS $$
BEGIN
	IF (NEW.temperatura < -1 OR NEW.temperatura > 8) THEN
		IF fn_excursion_sostenida(NEW.dispositivo_id, -1, 8, 10) THEN
		  INSERT INTO alerta(dispositivo_id, instante, tipo, detalle) VALUES (NEW.dispositivo_id, NEW.instante, 'excursion_termica', 'Temperatura fuera de rango sostenida 10 minutos');
		END IF;
	END IF;
	RETURN NEW;
END;
$$ LANGUAGE plpgsql;

DROP TRIGGER IF EXISTS tg_alerta_excursion ON lectura;
CREATE TRIGGER tg_alerta_excursion
AFTER INSERT ON lectura
FOR EACH ROW EXECUTE FUNCTION trg_alerta_excursion();
