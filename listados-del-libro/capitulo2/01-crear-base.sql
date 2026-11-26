-- Ejecutar UNA vez desde psql conectado al usuario postgres
CREATE USER agrocadena WITH PASSWORD 'cambiar_en_produccion';
CREATE DATABASE agrocadena OWNER agrocadena ENCODING 'UTF8';
GRANT ALL PRIVILEGES ON DATABASE agrocadena TO agrocadena;
