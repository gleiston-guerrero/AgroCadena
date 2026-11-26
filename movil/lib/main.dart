// lib/main.dart -- punto de entrada de la app movil AgroCadena
import 'package:firebase_core/firebase_core.dart';
import 'package:flutter/material.dart';
import 'screens/tablero_screen.dart';
import 'services/api_service.dart';
import 'services/notificaciones.dart';

Future<void> main() async {
	WidgetsFlutterBinding.ensureInitialized();
	try {
		// Requiere google-services.json (Android) del proyecto Firebase propio.
		await Firebase.initializeApp();
		escucharAlertas();
	} catch (_) {
		debugPrint("Firebase no configurado: la app funciona sin notificaciones push.");
	}
	runApp(const AgroCadenaApp());
}

class AgroCadenaApp extends StatelessWidget {
	const AgroCadenaApp({super.key});

	@override
	Widget build(BuildContext context) => MaterialApp(
		title: "AgroCadena",
		scaffoldMessengerKey: mensajero,
		theme: ThemeData(colorSchemeSeed: const Color(0xFF1B7F6B), useMaterial3: true),
		home: TableroScreen(api: TelemetriaApi()),
	);
}
