// lib/services/notificaciones.dart -- alertas push con Firebase Cloud Messaging (cap. 2)
import 'package:firebase_messaging/firebase_messaging.dart';
import 'package:flutter/material.dart';

final GlobalKey<ScaffoldMessengerState> mensajero = GlobalKey<ScaffoldMessengerState>();

void mostrarAlerta(String titulo, String cuerpo) {
	mensajero.currentState?.showSnackBar(SnackBar(
		backgroundColor: Colors.red.shade700,
		content: Text("$titulo\n$cuerpo"),
		duration: const Duration(seconds: 8),
	));
}

void escucharAlertas() {
	FirebaseMessaging.onMessage.listen((RemoteMessage m) {
		mostrarAlerta(m.notification?.title ?? "Alerta AgroCadena",
		m.notification?.body ?? "");
	});
}
