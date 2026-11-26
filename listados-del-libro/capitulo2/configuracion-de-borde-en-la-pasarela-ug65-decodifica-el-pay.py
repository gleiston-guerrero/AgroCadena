import json
import paho.mqtt.client as mqtt

# El servidor de red LoRaWAN entrega el frame del EM300-TH
def decodificar(payload_hex):
	b = bytes.fromhex(payload_hex)
	temperatura = int.from_bytes(b[0:2], "little", signed=True) / 10.0
	humedad = b[2] / 2.0
	return {"temp": temperatura, "hr": humedad}

def al_recibir(cliente, userdata, mensaje):
	trama = json.loads(mensaje.payload)
	lectura = decodificar(trama["data"])
	lectura["dispositivo"] = trama["devEUI"]
	cliente.publish("agrocadena/lectura", json.dumps(lectura), qos=1)

cliente = mqtt.Client(client_id="ug65-edge")
cliente.tls_set(ca_certs="ca.crt")
cliente.connect("broker.agrocadena.ec", 8883)
cliente.subscribe("lorawan/+/up")
cliente.on_message = al_recibir
cliente.loop_forever()
