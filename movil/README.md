# App móvil (Flutter 3 · Dart)

| Archivo | Función | Capítulo |
|---|---|---|
| `lib/services/api_service.dart` | Cliente REST: últimas lecturas y KPI | 2, 3 |
| `lib/services/notificaciones.dart` | Alertas push con Firebase Cloud Messaging | 2 |
| `lib/screens/tablero_screen.dart` | Pantalla de tablero | 3 |
| `lib/widgets/kpi_card.dart` | Tarjeta de indicador con semáforo | 3 |
| `lib/services/cache_local.dart` | Caché local con drift para trabajar sin conexión | 7 |

## Compilar y ejecutar

```bash
flutter pub get
dart run build_runner build --delete-conflicting-outputs   # genera cache_local.g.dart
flutter test
flutter run                                   # emulador Android: API en http://10.0.2.2:8080/v1
flutter run -d chrome --dart-define=API_BASE=http://localhost:8080/v1
```

## Notificaciones push (opcional)

1. Cree un proyecto en Firebase y registre la app Android `ec.uteq.agrocadena_movil`.
2. Copie `google-services.json` en `android/app/` (no lo suba al repositorio).
3. Sin esa configuración la app funciona igual, pero sin notificaciones push.
