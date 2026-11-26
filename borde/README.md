# Borde: pasarela Milesight UG65 (Python 3)

La UG65 ejecuta su servidor de red LoRaWAN, que publica cada trama en `lorawan/<devEUI>/up`. Estos programas corren en el entorno Python de la pasarela (o en cualquier equipo para pruebas).

| Archivo | Función | Capítulo |
|---|---|---|
| `decodificador.py` | Decodifica el *payload* del EM300-TH y publica la lectura en `agrocadena/lectura` con el formato del contrato `contracts/lectura-v1.schema.json` | 2 |
| `edge_predictor.py` | Regresión lineal sobre las últimas 12 lecturas; publica una pre-alerta en `agrocadena/pre-alerta` si prevé más de 8 °C en 5 minutos | 4 |
| `simulador_lorawan.py` | Genera tramas LoRaWAN de prueba sin hardware | — |

## Ejecutar

```bash
pip install -r requirements.txt
# PowerShell: $env:MQTT_PASSWORD="..."; $env:MQTT_CA="..\infra\mosquitto\certs\ca.crt"
export MQTT_PASSWORD=...            # contraseña del usuario ug65 (infra/.env)
export MQTT_CA=../infra/mosquitto/certs/ca.crt
python decodificador.py &
python edge_predictor.py &
python simulador_lorawan.py --temp 4.5 --n 3 --cada 1     # lecturas normales
python simulador_lorawan.py --temp 9.5 --n 3 --cada 1     # excursión: genera alertas
```

Variables opcionales: `MQTT_HOST` (por defecto `localhost`), `MQTT_PORT` (`8883`), `MQTT_USER` (`ug65`).

El formato del *payload* es el simplificado del libro (2 bytes de temperatura ×10 y 1 byte de humedad ×2). Para el formato real de Milesight (canales `03 67` y `04 68`) consulte el decodificador oficial del fabricante.
