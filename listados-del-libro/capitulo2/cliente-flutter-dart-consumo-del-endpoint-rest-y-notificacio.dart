// pubspec.yaml: dependencias http, firebase_messaging
import 'dart:convert';
import 'package:http/http.dart' as http;
import 'package:firebase_messaging/firebase_messaging.dart';

class TelemetriaApi {
	final String base = "https://api.agrocadena.ec/v1";
	final String token;          // JWT obtenido al iniciar sesion
	TelemetriaApi(this.token);
	
	Future<List<dynamic>> ultimasLecturas(String contenedor) async {
		final r = await http.get(
		Uri.parse("$base/contenedores/$contenedor/lecturas?limite=20"),
		headers: {"Authorization": "Bearer $token"},
		);
		if (r.statusCode != 200) throw Exception("HTTP ${r.statusCode}");
		return jsonDecode(r.body) as List<dynamic>;
	}
}

void escucharAlertas() {
	FirebaseMessaging.onMessage.listen((RemoteMessage m) {
		mostrarAlerta(m.notification?.title ?? "Alerta AgroCadena",
		m.notification?.body ?? "");
	});
}
