// movil/lib/services/api_service.dart
import 'dart:convert';

import 'package:http/http.dart' as http;

class TelemetriaApi {
  /// URL base del backend. En el emulador de Android, localhost del equipo
  /// es 10.0.2.2. Se puede cambiar con --dart-define=API_BASE=...
  final String base;
  final String? token; // JWT obtenido al iniciar sesion (perfil prod)
  TelemetriaApi({
    this.token,
    this.base = const String.fromEnvironment(
      "API_BASE",
      defaultValue: "http://10.0.2.2:8080/v1",
    ),
  });

  Map<String, String> get _cabeceras =>
      token == null ? {} : {"Authorization": "Bearer $token"};

  Future<List<dynamic>> ultimasLecturas(String contenedor) async {
    final r = await http.get(
      Uri.parse("$base/contenedores/$contenedor/lecturas?limite=20"),
      headers: _cabeceras,
    );
    if (r.statusCode != 200) throw Exception("HTTP ${r.statusCode}");
    return jsonDecode(r.body) as List<dynamic>;
  }

  /// KPI del contenedor (cap. 3): mkt, tor_min y cumplimiento.
  Future<Map<String, dynamic>> obtenerKpi(String contenedor) async {
    final r = await http.get(
      Uri.parse("$base/contenedores/$contenedor/kpi?ventana=PT24H"),
      headers: _cabeceras,
    );
    if (r.statusCode != 200) throw Exception("HTTP ${r.statusCode}");
    return jsonDecode(r.body) as Map<String, dynamic>;
  }
}
