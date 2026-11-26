-- db/02-funciones-y-triggers.sql
-- Ejecutar una vez, despues del primer arranque del backend.

-- 1) Hipertabla TimescaleDB sobre la tabla lectura (creada por Hibernate).
--    TimescaleDB exige que la clave primaria incluya la columna de
--    particion: la clave (id) se amplia a (id, instante).
CREATE EXTENSION IF NOT EXISTS timescaledb;
ALTER TABLE lectura DROP CONSTRAINT IF EXISTS lectura_pkey;
ALTER TABLE lectura ADD PRIMARY KEY (id, instante);
SELECT create_hypertable('lectura', 'instante',
                         if_not_exists => TRUE, migrate_data => TRUE);

-- 2) Excursion sostenida: la temperatura lleva al menos p_minutos fuera
--    del rango, sin ninguna lectura intermedia dentro del rango.
CREATE OR REPLACE FUNCTION fn_excursion_sostenida(
    p_dispositivo BIGINT,
    p_temp_min    NUMERIC,
    p_temp_max    NUMERIC,
    p_minutos     INT,
    p_instante    TIMESTAMPTZ)
  RETURNS BOOLEAN AS $$
DECLARE
  v_ultima_en_rango TIMESTAMPTZ;
  v_inicio          TIMESTAMPTZ;
BEGIN
  -- ultima lectura dentro del rango (se revisan las ultimas 24 horas)
  SELECT MAX(instante) INTO v_ultima_en_rango
    FROM lectura
   WHERE dispositivo_id = p_dispositivo
     AND instante BETWEEN p_instante - INTERVAL '1 day' AND p_instante
     AND temperatura BETWEEN p_temp_min AND p_temp_max;

  -- primera lectura fuera de rango despues de ella: inicio de la excursion
  SELECT MIN(instante) INTO v_inicio
    FROM lectura
   WHERE dispositivo_id = p_dispositivo
     AND instante BETWEEN p_instante - INTERVAL '1 day' AND p_instante
     AND (v_ultima_en_rango IS NULL OR instante > v_ultima_en_rango);

  RETURN v_inicio IS NOT NULL
     AND p_instante - v_inicio >= make_interval(mins => p_minutos);
END;
$$ LANGUAGE plpgsql;

-- 3) Trigger: al insertar una lectura fuera del rango de su contenedor,
--    registra una alerta si la excursion se sostiene 10 minutos
--    (RF-03). Mientras persista, repite la alerta cada 10 minutos.
CREATE OR REPLACE FUNCTION trg_alerta_excursion() RETURNS TRIGGER AS $$
DECLARE
  v_min NUMERIC;
  v_max NUMERIC;
BEGIN
  SELECT c.temp_min, c.temp_max INTO v_min, v_max
    FROM dispositivo d JOIN contenedor c ON c.id = d.contenedor_id
   WHERE d.id = NEW.dispositivo_id;

  IF (NEW.temperatura < v_min OR NEW.temperatura > v_max)
     AND fn_excursion_sostenida(NEW.dispositivo_id, v_min, v_max, 10,
                                NEW.instante)
     AND NOT EXISTS (SELECT 1 FROM alerta a
                      WHERE a.dispositivo_id = NEW.dispositivo_id
                        AND a.tipo = 'excursion_termica'
                        AND a.instante > NEW.instante - INTERVAL '10 minutes')
  THEN
    INSERT INTO alerta (dispositivo_id, instante, tipo, detalle)
    VALUES (NEW.dispositivo_id, NEW.instante, 'excursion_termica',
            'Temperatura fuera de rango sostenida 10 minutos');
  END IF;
  RETURN NEW;
END;
$$ LANGUAGE plpgsql;

DROP TRIGGER IF EXISTS tg_alerta_excursion ON lectura;
CREATE TRIGGER tg_alerta_excursion
  AFTER INSERT ON lectura
  FOR EACH ROW EXECUTE FUNCTION trg_alerta_excursion();
