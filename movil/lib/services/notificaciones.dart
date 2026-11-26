// movil/lib/services/notificaciones.dart
import 'package:firebase_messaging/firebase_messaging.dart';
import 'package:flutter/material.dart';

final GlobalKey<ScaffoldMessengerState> mensajero =
    GlobalKey<ScaffoldMessengerState>();

void mostrarAlerta(String titulo, String cuerpo) {
  mensajero.currentState?.showSnackBar(
    SnackBar(
      backgroundColor: Colors.red.shade700,
      content: Text("$titulo\n$cuerpo"),
      duration: const Duration(seconds: 8),
    ),
  );
}

/// El backend publica cada alerta en el topico "alertas" de FCM.
Future<void> escucharAlertas() async {
  final fcm = FirebaseMessaging.instance;
  await fcm.requestPermission();
  await fcm.subscribeToTopic("alertas");
  FirebaseMessaging.onMessage.listen((RemoteMessage m) {
    mostrarAlerta(
      m.notification?.title ?? "Alerta AgroCadena",
      m.notification?.body ?? "",
    );
  });
}
