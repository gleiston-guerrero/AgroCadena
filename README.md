# Proyecto AgroCadena

Código fuente completo del caso integrador del libro *Guía para el desarrollo de sistemas de información basados en Internet de las cosas. Una mirada desde la perspectiva de los desarrolladores y arquitectos de software* (Guerrero Ulloa, Brito Casanova y Zúñiga Paredes, Editorial UTEQ, 2026).

AgroCadena monitoriza la cadena de frío de contenedores refrigerados de banano y cacao: sensores LoRaWAN, una pasarela de borde, un *broker* MQTT con TLS, un *backend* Spring Boot sobre PostgreSQL/TimescaleDB, un tablero web en Vue 3 y una aplicación móvil en Flutter. El libro lo construye por partes a lo largo de los nueve capítulos; este repositorio lo reúne como un proyecto que se compila y se ejecuta.

## Estructura

| Carpeta | Contenido | Capítulos |
|---|---|---|
| `backend/` | API REST y consumidor MQTT en Spring Boot 3.3 con Java 21 (Maven Wrapper, pruebas JUnit 5, Dockerfile) | 2, 3, 7, 8, 9 |
| `db/` | Scripts SQL: base de datos, hipertabla TimescaleDB, *trigger* de excursión térmica y datos de demostración | 2 |
| `borde/` | Lógica de la pasarela UG65 en Python: decodificador LoRaWAN, predictor de borde y simulador de tramas | 2, 4 |
| `web/` | Tablero web en Vue 3 con Chart.js y Leaflet (Vite, Dockerfile con nginx) | 3 |
| `movil/` | Aplicación Flutter/Dart: tablero de KPI, notificaciones FCM y caché local con drift | 2, 3, 7 |
| `contracts/` | Contrato JSON Schema del mensaje de telemetría | 5 |
| `features/` | Criterios de aceptación en Gherkin (HU-03) | 6 |
| `infra/` | Docker Compose, Mosquitto con TLS, manifiestos de Kubernetes (K3s), reglas de Prometheus y scripts de operación | 8, 9 |
| `docs/` | *Runbook* de operación y archivos del problema guía del capítulo 8 | 8, 9 |
| `.github/workflows/` | Integración continua y publicación de imágenes | 9 |

Cada carpeta tiene su propio `README.md` con las instrucciones para compilar y ejecutar ese componente.

## Inicio rápido (Windows, macOS o Linux)

Requisitos: Docker Desktop, Git (en Windows, Git Bash para los scripts `.sh`), Java 21, Node.js 20 y, para la app móvil, Flutter 3.

```bash
git clone https://github.com/gleiston-guerrero/AgroCadena.git
cd AgroCadena/infra
cp .env.example .env                    # cambie las contraseñas
./mosquitto/generar-certs.sh            # CA propia y certificado del broker
./mosquitto/crear-usuarios.sh           # usuarios MQTT backend y ug65
docker compose up -d --build            # postgres, mosquitto, backend y web
# cuando el backend haya arrancado (Hibernate crea las tablas):
docker compose exec -T postgres psql -U agrocadena -d agrocadena < ../db/02-funciones-y-triggers.sql
docker compose exec -T postgres psql -U agrocadena -d agrocadena < ../db/03-datos-demo.sql
```

Envíe lecturas simuladas desde la pasarela (ver `borde/README.md`) y abra:

- Tablero web: <http://localhost:8081>
- API: <http://localhost:8080/v1/contenedores/CNT-007/lecturas> y `.../CNT-007/kpi`
- Documentación OpenAPI: <http://localhost:8080/swagger-ui.html>
- Salud y métricas: <http://localhost:8080/actuator/health>, `/actuator/prometheus`

En desarrollo la API no exige JWT; con el perfil `prod` (`SPRING_PROFILES_ACTIVE=prod`) se activa la cadena de seguridad del capítulo 8.

## Correspondencia entre los listados del libro y el proyecto

Cada listado del libro que pertenece a AgroCadena es una copia exacta de un archivo de este repositorio, completo o en un fragmento contiguo, y su título indica la ruta del archivo. El repositorio contiene además el código que el libro no imprime para no extenderse. El script `verificar_sincronia.py` de las fuentes del libro comprueba esta correspondencia línea por línea.

| Cap. | Etiqueta | Título del listado | Archivo | Alcance |
|---|---|---|---|---|
| 2 | `lst:agro2-py` | Decodificador de borde en la pasarela UG65: decodifica el payload LoRaWAN y lo publica por MQTT | `borde/decodificador.py` | completo |
| 2 | `lst:agro2-pom` | Dependencias del capítulo 2 en el POM del backend | `backend/pom.xml` | líneas 31–70 |
| 2 | `lst:agro2-appyml` | Parámetros de base de datos y de MQTT externalizados | `backend/src/main/resources/application.yml` | líneas 1–21 |
| 2 | `lst:agro2-bd` | Creación de la base de datos y del usuario | `db/01-crear-base.sql` | completo |
| 2 | `lst:agro2-funcs` | Hipertabla, función de excursión sostenida y trigger de alerta; se ejecuta tras el primer arranque | `db/02-funciones-y-triggers.sql` | completo |
| 2 | `lst:agro2-traza` | Anotación de trazabilidad de requisitos | `backend/src/main/java/ec/uteq/agrocadena/traza/Requisito.java` | completo |
| 2 | `lst:agro2-ent` | Entidad Lectura: mapeo y validaciones | `backend/src/main/java/ec/uteq/agrocadena/domain/Lectura.java` | líneas 1–46 |
| 2 | `lst:agro2-repository` | Repositorio Spring Data JPA de lecturas | `backend/src/main/java/ec/uteq/agrocadena/persistence/LecturaRepository.java` | completo |
| 2 | `lst:agro2-dto` | DTO de ingreso | `backend/src/main/java/ec/uteq/agrocadena/web/dto/IngresoDto.java` | completo |
| 2 | `lst:agro2-rules` | Reglas de calidad de la lectura | `backend/src/main/java/ec/uteq/agrocadena/service/ReglasCalidad.java` | completo |
| 2 | `lst:agro2-service` | Servicio de telemetría | `backend/src/main/java/ec/uteq/agrocadena/service/TelemetriaService.java` | completo |
| 2 | `lst:agro2-mqtt` | Configuración MQTT con Spring Integration | `backend/src/main/java/ec/uteq/agrocadena/config/MqttConfig.java` | líneas 25–75 |
| 2 | `lst:agro2-consumidor-mqtt` | Consumidor MQTT | `backend/src/main/java/ec/uteq/agrocadena/messaging/ConsumidorMqtt.java` | completo |
| 2 | `lst:agro2-ctl` | DTO de salida | `backend/src/main/java/ec/uteq/agrocadena/web/dto/LecturaDto.java` | completo |
| 2 | `lst:agro2-ctlrest` | Controlador REST de salida | `backend/src/main/java/ec/uteq/agrocadena/web/controller/ContenedorController.java` | completo |
| 2 | `lst:agro2-test` | Prueba unitaria de las reglas de calidad (JUnit 5) | `backend/src/test/java/ec/uteq/agrocadena/service/ReglasCalidadTest.java` | completo |
| 2 | `lst:agro2-flutter` | Cliente Flutter/Dart del endpoint REST con JWT | `movil/lib/services/api_service.dart` | completo |
| 2 | `lst:agro2-flutter-notif` | Recepción de alertas push con Firebase Cloud Messaging | `movil/lib/services/notificaciones.dart` | completo |
| 3 | `lst:agro3-kpi` | Servicio de KPIs: MKT, TOR y tasa de cumplimiento | `backend/src/main/java/ec/uteq/agrocadena/service/KpiService.java` | completo |
| 3 | `lst:agro3-ctl` | Endpoint REST de KPIs | `backend/src/main/java/ec/uteq/agrocadena/web/controller/KpiController.java` | completo |
| 3 | `lst:agro3-api-js` | Cliente HTTP del tablero web con axios y JWT | `web/src/services/api.js` | completo |
| 3 | `lst:agro3-tablero-vue` | Vista de tablero (Vue 3, Composition API) | `web/src/views/Tablero.vue` | completo |
| 3 | `lst:agro3-flutter` | Pantalla móvil de tablero que consume el endpoint de KPIs | `movil/lib/screens/tablero_screen.dart` | completo |
| 4 | `lst:agro4-edge-py` | Predicción de excursión en la pasarela | `borde/edge_predictor.py` | completo |
| 5 | `lst:agro5-schema` | Contrato JSON Schema del payload | `contracts/lectura-v1.schema.json` | completo |
| 6 | `lst:agro6-gherkin` | Criterios de aceptación de HU-03 | `features/excursion-termica.feature` | completo |
| 7 | `lst:agro7-cb` | Circuit breaker para la persistencia de telemetría | `backend/src/main/java/ec/uteq/agrocadena/config/ResilienceConfig.java` | completo |
| 7 | `lst:agro7-svc` | Aplicación del circuit breaker | `backend/src/main/java/ec/uteq/agrocadena/service/TelemetriaServiceResiliente.java` | completo |
| 7 | `lst:agro7-cache` | Buffer local con drift en Flutter | `movil/lib/services/cache_local.dart` | completo |
| 8 | `lst:guia-mosquitto` | Problema guía: broker reducido a MQTT sobre TLS (se instala en /etc/mosquitto/conf.d/) | `docs/problema-guia-cap8/laboratorio.conf` | completo |
| 8 | `lst:guia-acl` | Problema guía: autorización por tópico (se instala en /etc/mosquitto/acl) | `docs/problema-guia-cap8/acl` | completo |
| 8 | `lst:agro8-sec` | Cadena de filtros de Spring Security | `backend/src/main/java/ec/uteq/agrocadena/config/SecurityConfig.java` | líneas 14–75 |
| 9 | `lst:agro9-k8s` | Despliegue y servicio del backend en K3s | `infra/k8s/backend.yaml` | completo |
| 9 | `lst:agro9-compose` | Composición de servicios para desarrollo | `infra/docker-compose.yml` | completo |
| 9 | `lst:agro9-docker` | Imagen multietapa del backend | `backend/Dockerfile` | completo |
| 9 | `lst:agro9-ci` | Canalización CI/CD/CM del backend | `.github/workflows/ci.yml` | líneas 1–51 |
| 9 | `lst:agro9-actuator` | Sección de observabilidad | `backend/src/main/resources/application.yml` | líneas 39–57 |
| 9 | `lst:agro9-metricas` | Medición de la latencia de alerta | `backend/src/main/java/ec/uteq/agrocadena/service/MetricasAlerta.java` | completo |
| 9 | `lst:agro9-reglas` | Alertas de Prometheus sobre los SLO | `infra/prometheus/reglas-agrocadena.yml` | completo |
| 9 | `lst:agro9-runbook-md` | Entrada del runbook | `docs/runbook/latencia-alerta.md` | completo |
| 9 | `lst:agro9-baja` | Baja segura de un dispositivo | `infra/ops/baja-dispositivo.sh` | completo |
| 9 | `lst:agro9-traza-test` | Verificación automática de la matriz de trazabilidad | `backend/src/test/java/ec/uteq/agrocadena/traza/TrazabilidadTest.java` | completo |

## Código del proyecto que el libro no imprime

- **Clases de dominio y repositorios.** `Contenedor`, `Dispositivo`, `Alerta`, sus repositorios y los métodos de acceso de `Lectura`.
- **Detección de excursión y alertas.** `DetectorExcursion` aplica en Java la misma regla que el *trigger* de `db/02-funciones-y-triggers.sql` (temperatura fuera del rango del contenedor durante al menos 10 minutos, sin lecturas intermedias dentro del rango); `AlertaService` envía cada alerta nueva a la app móvil por Firebase Cloud Messaging (`NotificadorFcm`, tópico `alertas`) y `EstadoController` expone el semáforo del contenedor en `/v1/contenedores/{codigo}/estado`.
- **Resiliencia.** `ColaReintento` guarda las lecturas que no pudieron persistirse mientras el *circuit breaker* está abierto.
- **Salud.** `MqttSalud` informa el estado de la conexión con el *broker* al grupo de *readiness* de Actuator.
- **Seguridad en desarrollo.** `SecurityConfig` (cap. 8) se activa con el perfil `prod`; `SecurityDevConfig` deja la API abierta solo para desarrollo local.
- **Pruebas de aceptación.** `AceptacionTest` y `ExcursionTermicaPasos` ejecutan con Cucumber los escenarios de `features/excursion-termica.feature`.
- **Datos de demostración y simulador.** `db/03-datos-demo.sql` y `borde/simulador_lorawan.py`.

## Licencia

Código publicado con fines educativos junto con la obra. Ver `LICENSE`.
