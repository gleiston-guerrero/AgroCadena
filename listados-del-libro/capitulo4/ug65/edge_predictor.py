# ug65/edge_predictor.py
# Modelo predictivo de borde: regresion lineal sobre la ventana
# deslizante de las ultimas N=12 lecturas; publica pre-alerta MQTT.

from collections import deque
import json, time
import paho.mqtt.client as mqtt

N         = 12     # 1 hora a 1 lectura cada 5 min
HORIZONTE = 5      # min hacia adelante
T_MAX     = 8.0    # grados C

ventana = deque(maxlen=N)

def pendiente(serie):
	n = len(serie)
	if n < 3: return 0.0
	x_media = (n - 1) / 2.0
	y_media = sum(serie) / n
	num = sum((i - x_media) * (y - y_media) for i, y in enumerate(serie))
	den = sum((i - x_media) ** 2 for i in range(n))
	return num / den if den else 0.0

def predecir(actual, m):
	return actual + m * HORIZONTE

def al_recibir(cliente, _ud, msg):
	lectura = json.loads(msg.payload)
	ventana.append(lectura["temperatura"])
	m   = pendiente(list(ventana))
	pre = predecir(lectura["temperatura"], m)
	if pre > T_MAX:
		cliente.publish("agrocadena/pre-alerta",
			json.dumps({"dispositivo": lectura["dispositivo"],
				"instante":    lectura["instante"],
					"prediccion":  pre, "horizonte_min": HORIZONTE}),
			qos=1)

cliente = mqtt.Client(client_id="ug65-predictor")
cliente.tls_set(ca_certs="ca.crt")
cliente.connect("broker.agrocadena.ec", 8883)
cliente.subscribe("agrocadena/lectura")
cliente.on_message = al_recibir
cliente.loop_forever()
