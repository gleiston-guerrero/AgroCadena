# borde/decodificador.py -- logica de borde en la pasarela UG65 (cap. 2, lst:agro2-py)
# Recibe las tramas LoRaWAN del servidor de red integrado, decodifica el payload
# del EM300-TH y publica la lectura en el formato del contrato
# contracts/lectura-v1.schema.json (cap. 5).
import json
import os
from datetime import datetime, timezone

import paho.mqtt.client as mqtt

BROKER = os.getenv("MQTT_HOST", "localhost")
PUERTO = int(os.getenv("MQTT_PORT", "8883"))
CA     = os.getenv("MQTT_CA", "ca.crt")
USUARIO = os.getenv("MQTT_USER", "ug65")
CLAVE   = os.environ["MQTT_PASSWORD"]


def decodificar(payload_hex):
	"""Formato simplificado del libro: 2 bytes de temperatura (x10, little endian
	con signo) y 1 byte de humedad relativa (x2)."""
	b = bytes.fromhex(payload_hex)
	temperatura = int.from_bytes(b[0:2], "little", signed=True) / 10.0
	humedad = b[2] / 2.0
	return {"temperatura": temperatura, "humedad": humedad}


def al_recibir(cliente, userdata, mensaje):
	trama = json.loads(mensaje.payload)
	lectura = decodificar(trama["data"])
	lectura["dispositivo"] = trama["devEUI"].upper()
	lectura["instante"] = datetime.now(timezone.utc).isoformat(timespec="seconds")
	cliente.publish("agrocadena/lectura", json.dumps(lectura), qos=1)


def main():
	cliente = mqtt.Client(mqtt.CallbackAPIVersion.VERSION2, client_id="ug65-edge")
	cliente.username_pw_set(USUARIO, CLAVE)
	cliente.tls_set(ca_certs=CA)
	cliente.on_message = al_recibir
	cliente.connect(BROKER, PUERTO)
	cliente.subscribe("lorawan/+/up", qos=1)
	cliente.loop_forever()


if __name__ == "__main__":
	main()
