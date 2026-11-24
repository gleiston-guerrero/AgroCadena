# Proyecto AgroCadena

Código fuente del caso integrador del libro *Guía para el desarrollo de sistemas de información basados en Internet de las cosas. Una mirada desde la perspectiva de los desarrolladores y arquitectos de software* (Guerrero Ulloa, Brito Casanova y Zúñiga Paredes, Editorial UTEQ).

AgroCadena es un sistema IoT de monitorización de la cadena de frío de contenedores refrigerados de banano y cacao. Se construye de forma incremental en los nueve capítulos del libro.

## Organización

Cada carpeta `capituloN/` contiene los archivos que el capítulo N presenta como listados, con la ruta que indica el título del listado en el libro. Los nombres de archivo derivados de títulos sin ruta explícita se generaron a partir del título del listado.

| Cap. | Etiqueta en el libro | Archivo | Título del listado |
|---|---|---|---|
| 2 | `lst:agro2-py` | `capitulo2/configuracion-de-borde-en-la-pasarela-ug65-decodifica-el-pay.py` | Configuracion de borde en la pasarela UG65: decodifica el payload y publica por MQTT. |
| 2 | `lst:agro2-pom` | `capitulo2/pom.xml` | Extracto de pom.xml. |
| 2 | `lst:agro2-appyml` | `capitulo2/application.yml` | Extracto de application.yml. |
| 2 | `lst:agro2-bd` | `capitulo2/01-crear-base.sql` | Script 01-crear-base.sql: solo crea la base de datos y el usuario. |
| 2 | `lst:agro2-funcs` | `capitulo2/02-funciones-y-triggers.sql` | Script 02-funciones-y-triggers.sql: ejecutar tras el primer arranque. |
| 2 | `lst:agro2-traza` | `capitulo2/anotacion-de-trazabilidad-de-requisitos-paquete-traza.java` | Anotacion de trazabilidad de requisitos (paquete traza). |
| 2 | `lst:agro2-ent` | `capitulo2/entidad-lectura-y-repositorio-spring-data-jpa.java` | Entidad Lectura y repositorio Spring Data JPA. |
| 2 | `lst:agro2-repository` | `capitulo2/repositorio-spring-data-jpa.java` | Repositorio Spring Data JPA. |
| 2 | `lst:agro2-dto` | `capitulo2/dto-de-ingreso.java` | DTO de ingreso. |
| 2 | `lst:agro2-rules` | `capitulo2/reglas-de-calidad.java` | Reglas de calidad. |
| 2 | `lst:agro2-service` | `capitulo2/servicio-de-telemetria.java` | Servicio de telemetria. |
| 2 | `lst:agro2-mqtt` | `capitulo2/configuracion-mqtt-spring-integration.java` | Configuracion MQTT (Spring Integration) |
| 2 | `lst:agro2-consumidor-mqtt` | `capitulo2/configuracion-mqtt-consumidor.java` | Configuracion MQTT consumidor. |
| 2 | `lst:agro2-ctl` | `capitulo2/dto-de-salida.java` | DTO de salida. |
| 2 | `lst:agro3-ctl` | `capitulo2/controlador-rest-de-salida.java` | Controlador REST de salida. |
| 2 | `lst:agro2-test` | `capitulo2/prueba-unitaria-de-las-reglas-de-calidad-junit-5.java` | Prueba unitaria de las reglas de calidad (JUnit 5). |
| 2 | `lst:agro2-flutter` | `capitulo2/cliente-flutter-dart-consumo-del-endpoint-rest-y-notificacio.dart` | Cliente Flutter/Dart: consumo del endpoint REST y notificaciones push. |
| 3 | `lst:agro3-kpi` | `capitulo3/servicio-de-kpis-en-spring-boot-mkt-tor-y-tasa-de-cumplimien.java` | Servicio de KPIs en Spring Boot: MKT, TOR y tasa de cumplimiento. |
| 3 | `lst:agro3-ctl` | `capitulo3/endpoint-rest-de-kpis-ampliacion-del-controlador-del-capitul.java` | Endpoint REST de KPIs (ampliacion del controlador del capitulo 2). |
| 3 | `lst:agro3-api-js` | `capitulo3/src/services/api.js` | Archivo: src/services/api.js --- cliente HTTP con axios y JWT. |
| 3 | `lst:agro3-tablero-vue` | `capitulo3/src/views/Tablero.vue` | Archivo: src/views/Tablero.vue --- vista de tablero (Vue 3, Composition API). |
| 3 | `lst:agro3-flutter` | `capitulo3/pantalla-movil-de-tablero-flutter-dart-que-consume-el-endpoi.dart` | Pantalla movil de tablero (Flutter/Dart) que consume el endpoint de KPIs. |
| 4 | `lst:agro4-edge-py` | `capitulo4/ug65/edge_predictor.py` | Archivo: ug65/edge_predictor.py --- prediccion de excursion en la pasarela. |
| 5 | `lst:agro5-schema` | `capitulo5/contracts/lectura-v1.schema.json` | Archivo: contracts/lectura-v1.schema.json --- contrato JSON Schema del payload. |
| 6 | `lst:agro6-gherkin` | `capitulo6/features/excursion-termica.feature` | Archivo: features/excursion-termica.feature --- criterios de aceptacion HU-03. |
| 7 | `lst:agro7-cb` | `capitulo7/config/ResilienceConfig.java` | Archivo: config/ResilienceConfig.java --- circuit breaker para el consumidor MQTT. |
| 7 | `lst:agro7-svc` | `capitulo7/service/TelemetriaServiceResiliente.java` | Archivo: service/ TelemetriaService Resiliente.java --- aplicacion del CB. |
| 7 | `lst:agro7-cache` | `capitulo7/lib/services/cache_local.dart` | Archivo: lib/services/cache_local.dart --- buffer local con drift en Flutter. |
| 8 | `lst:guia-mosquitto` | `capitulo8/laboratorio.conf` | Archivo: laboratorio.conf (en /etc/mosquitto/conf.d/) --- broker reducido a MQTT sobre TLS. |
| 8 | `lst:guia-acl` | `capitulo8/etc/mosquitto/acl` | Archivo: /etc/mosquitto/acl --- autorizacion por topico. |
| 8 | `lst:agro8-sec` | `capitulo8/config/SecurityConfig.java` | Archivo: config/SecurityConfig.java --- cadena de filtros Spring Security. |
| 9 | `lst:agro9-k8s` | `capitulo9/k8s/backend.yaml` | Archivo: k8s/backend.yaml --- despliegue y servicio del backend en K3s. |
| 9 | `lst:agro9-compose` | `capitulo9/docker-compose.yml` | Archivo: docker-compose.yml --- composicion de servicios para desarrollo. |
| 9 | `lst:agro9-docker` | `capitulo9/agrocadena-backend/Dockerfile` | Archivo: agrocadena-backend/Dockerfile --- imagen multi-etapa del backend. |
| 9 | `lst:agro9-ci` | `capitulo9/.github/workflows/ci.yml` | Archivo: .github/workflows/ci.yml --- canalizacion CI/CD/CM. |
| 9 | `lst:agro9-actuator` | `capitulo9/application.yml` | Archivo: application.yml --- seccion de observabilidad. |
| 9 | `lst:agro9-metricas` | `capitulo9/service/MetricasAlerta.java` | Archivo: service/MetricasAlerta.java --- medicion de la latencia de alerta. |
| 9 | `lst:agro9-reglas` | `capitulo9/prometheus/reglas-agrocadena.yml` | Archivo: prometheus/reglas-agrocadena.yml --- alertas sobre los SLO. |
| 9 | `lst:agro9-runbook-md` | `capitulo9/docs/runbook/latencia-alerta.md` | Archivo: docs/runbook/latencia-alerta.md --- entrada del runbook. |
| 9 | `lst:agro9-baja` | `capitulo9/ops/baja-dispositivo.sh` | Archivo: ops/baja-dispositivo.sh --- baja segura de un dispositivo. |
| 9 | `lst:agro9-traza-test` | `capitulo9/traza/TrazabilidadTest.java` | Archivo: traza/TrazabilidadTest.java --- verificacion automatica de la matriz. |

## Componentes y tecnologías

- Sensores Milesight EM300-TH (temperatura y humedad) y WS301 (contacto de puerta); pasarela Milesight UG65; rastreador Teltonika FMC130.
- *Backend* Spring Boot con Java 21, PostgreSQL y TimescaleDB.
- Mensajería MQTT con Mosquitto sobre TLS.
- Cliente web Vue 3 con Chart.js y Leaflet; aplicación móvil Flutter/Dart con Firebase Cloud Messaging.
- Despliegue con Docker Compose y K3s; integración continua con GitHub Actions; observabilidad con Prometheus.

## Uso

Los archivos corresponden a los listados del libro y se agrupan por el capítulo en que se introducen. Algunos listados del libro son extractos (por ejemplo, `pom.xml` y `application.yml`); en esos casos el archivo contiene el extracto publicado.

## Licencia

Código publicado con fines educativos junto con la obra (CC BY-NC-ND 4.0 para el texto del libro).
