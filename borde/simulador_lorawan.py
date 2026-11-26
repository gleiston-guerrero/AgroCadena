# borde/simulador_lorawan.py -- genera tramas LoRaWAN de prueba (sin hardware).
# Publica en lorawan/<devEUI>/up como lo haria el servidor de red de la UG65.
# Uso: python simulador_lorawan.py --temp 9.5 --hr 80 --n 3 --cada 1
import argparse, json, os, time
import paho.mqtt.client as mqtt

p = argparse.ArgumentParser()
p.add_argument("--dev", default="24E124136B316001")
p.add_argument("--temp", type=float, default=4.5)
p.add_argument("--hr", type=float, default=85.0)
p.add_argument("--n", type=int, default=1)
p.add_argument("--cada", type=float, default=5.0, help="segundos entre tramas")
a = p.parse_args()

def codificar(t, hr):
	return (int(round(t * 10)).to_bytes(2, "little", signed=True) + bytes([int(hr * 2)])).hex()

c = mqtt.Client(mqtt.CallbackAPIVersion.VERSION2, client_id="simulador")
c.username_pw_set(os.getenv("MQTT_USER", "ug65"), os.environ["MQTT_PASSWORD"])
c.tls_set(ca_certs=os.getenv("MQTT_CA", "ca.crt"))
c.connect(os.getenv("MQTT_HOST", "localhost"), int(os.getenv("MQTT_PORT", "8883")))
c.loop_start()
for i in range(a.n):
	trama = {"devEUI": a.dev, "data": codificar(a.temp, a.hr)}
	c.publish(f"lorawan/{a.dev}/up", json.dumps(trama), qos=1).wait_for_publish()
	print("enviada", trama)
	time.sleep(a.cada)
c.loop_stop(); c.disconnect()
