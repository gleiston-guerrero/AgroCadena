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
| `docs/` | *Runbook* de operación | 9 |
| `.github/workflows/` | Integración continua y publicación de imágenes | 9 |
| `listados-del-libro/` | Los 41 listados tal como aparecen impresos, organizados por capítulo | 2–9 |

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

| Cap. | Etiqueta | Título del listado | Tal como está impreso | En el proyecto |
|---|---|---|---|---|
| 2 | `lst:agro2-py` | Configuración de borde en la pasarela UG65: decodifica el payload y publica por MQTT. | `listados-del-libro/capitulo2/configuracion-de-borde-en-la-pasarela-ug65-decodifica-el-pay.py` | `borde/decodificador.py` |
| 2 | `lst:agro2-pom` | Extracto de pom.xml. | `listados-del-libro/capitulo2/pom.xml` | `backend/pom.xml` |
| 2 | `lst:agro2-appyml` | Extracto de application.yml. | `listados-del-libro/capitulo2/application.yml` | `backend/src/main/resources/application.yml` |
| 2 | `lst:agro2-bd` | Script 01-crear-base.sql: solo crea la base de datos y el usuario. | `listados-del-libro/capitulo2/01-crear-base.sql` | `db/01-crear-base.sql` |
| 2 | `lst:agro2-funcs` | Script 02-funciones-y-triggers.sql: ejecutar tras el primer arranque. | `listados-del-libro/capitulo2/02-funciones-y-triggers.sql` | `db/02-funciones-y-triggers.sql` |
| 2 | `lst:agro2-traza` | Anotación de trazabilidad de requisitos (paquete traza). | `listados-del-libro/capitulo2/anotacion-de-trazabilidad-de-requisitos-paquete-traza.java` | `backend/src/main/java/ec/uteq/agrocadena/traza/Requisito.java` |
| 2 | `lst:agro2-ent` | Entidad Lectura y repositorio Spring Data JPA. | `listados-del-libro/capitulo2/entidad-lectura-y-repositorio-spring-data-jpa.java` | `backend/src/main/java/ec/uteq/agrocadena/domain/Lectura.java` |
| 2 | `lst:agro2-repository` | Repositorio Spring Data JPA. | `listados-del-libro/capitulo2/repositorio-spring-data-jpa.java` | `backend/src/main/java/ec/uteq/agrocadena/persistence/LecturaRepository.java` |
| 2 | `lst:agro2-dto` | DTO de ingreso. | `listados-del-libro/capitulo2/dto-de-ingreso.java` | `backend/src/main/java/ec/uteq/agrocadena/web/dto/IngresoDto.java` |
| 2 | `lst:agro2-rules` | Reglas de calidad. | `listados-del-libro/capitulo2/reglas-de-calidad.java` | `backend/src/main/java/ec/uteq/agrocadena/service/ReglasCalidad.java` |
| 2 | `lst:agro2-service` | Servicio de telemetría. | `listados-del-libro/capitulo2/servicio-de-telemetria.java` | `backend/src/main/java/ec/uteq/agrocadena/service/TelemetriaService.java` |
| 2 | `lst:agro2-mqtt` | Configuración MQTT (Spring Integration) | `listados-del-libro/capitulo2/configuracion-mqtt-spring-integration.java` | `backend/src/main/java/ec/uteq/agrocadena/config/MqttConfig.java` |
| 2 | `lst:agro2-consumidor-mqtt` | Configuración MQTT consumidor. | `listados-del-libro/capitulo2/configuracion-mqtt-consumidor.java` | `backend/src/main/java/ec/uteq/agrocadena/messaging/ConsumidorMqtt.java` |
| 2 | `lst:agro2-ctl` | DTO de salida. | `listados-del-libro/capitulo2/dto-de-salida.java` | `backend/src/main/java/ec/uteq/agrocadena/web/dto/LecturaDto.java` |
| 2 | `lst:agro2-ctlrest` | Controlador REST de salida. | `listados-del-libro/capitulo2/controlador-rest-de-salida.java` | `backend/src/main/java/ec/uteq/agrocadena/web/controller/ContenedorController.java` |
| 2 | `lst:agro2-test` | Prueba unitaria de las reglas de calidad (JUnit 5). | `listados-del-libro/capitulo2/prueba-unitaria-de-las-reglas-de-calidad-junit-5.java` | `backend/src/test/java/ec/uteq/agrocadena/service/ReglasCalidadTest.java` |
| 2 | `lst:agro2-flutter` | Cliente Flutter/Dart: consumo del endpoint REST y notificaciones push. | `listados-del-libro/capitulo2/cliente-flutter-dart-consumo-del-endpoint-rest-y-notificacio.dart` | `movil/lib/services/api_service.dart`, `movil/lib/services/notificaciones.dart` |
| 3 | `lst:agro3-kpi` | Servicio de KPIs en Spring Boot: MKT, TOR y tasa de cumplimiento. | `listados-del-libro/capitulo3/servicio-de-kpis-en-spring-boot-mkt-tor-y-tasa-de-cumplimien.java` | `backend/src/main/java/ec/uteq/agrocadena/service/KpiService.java` |
| 3 | `lst:agro3-ctl` | Endpoint REST de KPIs (ampliación del controlador del capítulo 2). | `listados-del-libro/capitulo3/endpoint-rest-de-kpis-ampliacion-del-controlador-del-capitul.java` | `backend/src/main/java/ec/uteq/agrocadena/web/controller/KpiController.java` |
| 3 | `lst:agro3-api-js` | Archivo: src/services/api.js --- cliente HTTP con axios y JWT. | `listados-del-libro/capitulo3/src/services/api.js` | `web/src/services/api.js` |
| 3 | `lst:agro3-tablero-vue` | Archivo: src/views/Tablero.vue --- vista de tablero (Vue 3, Composition API). | `listados-del-libro/capitulo3/src/views/Tablero.vue` | `web/src/views/Tablero.vue` |
| 3 | `lst:agro3-flutter` | Pantalla móvil de tablero (Flutter/Dart) que consume el endpoint de KPIs. | `listados-del-libro/capitulo3/pantalla-movil-de-tablero-flutter-dart-que-consume-el-endpoi.dart` | `movil/lib/screens/tablero_screen.dart` |
| 4 | `lst:agro4-edge-py` | Archivo: ug65/edge_predictor.py --- predicción de excursión en la pasarela. | `listados-del-libro/capitulo4/ug65/edge_predictor.py` | `borde/edge_predictor.py` |
| 5 | `lst:agro5-schema` | Archivo: contracts/lectura-v1.schema.json --- contrato JSON Schema del payload. | `listados-del-libro/capitulo5/contracts/lectura-v1.schema.json` | `contracts/lectura-v1.schema.json` |
| 6 | `lst:agro6-gherkin` | Archivo: features/excursion-termica.feature --- criterios de aceptación HU-03. | `listados-del-libro/capitulo6/features/excursion-termica.feature` | `features/excursion-termica.feature` |
| 7 | `lst:agro7-cb` | Archivo: config/ResilienceConfig.java --- circuit breaker para el consumidor MQTT. | `listados-del-libro/capitulo7/config/ResilienceConfig.java` | `backend/src/main/java/ec/uteq/agrocadena/config/ResilienceConfig.java` |
| 7 | `lst:agro7-svc` | Archivo: service/ TelemetriaService Resiliente.java --- aplicación del CB. | `listados-del-libro/capitulo7/service/TelemetriaServiceResiliente.java` | `backend/src/main/java/ec/uteq/agrocadena/service/TelemetriaServiceResiliente.java` |
| 7 | `lst:agro7-cache` | Archivo: lib/services/cache_local.dart --- buffer local con drift en Flutter. | `listados-del-libro/capitulo7/lib/services/cache_local.dart` | `movil/lib/services/cache_local.dart` |
| 8 | `lst:guia-mosquitto` | Archivo: laboratorio.conf (en /etc/mosquitto/conf.d/) --- broker reducido a MQTT sobre TLS. | `listados-del-libro/capitulo8/laboratorio.conf` | `infra/mosquitto/mosquitto.conf` |
| 8 | `lst:guia-acl` | Archivo: /etc/mosquitto/acl --- autorización por tópico. | `listados-del-libro/capitulo8/etc/mosquitto/acl` | `infra/mosquitto/acl` |
| 8 | `lst:agro8-sec` | Archivo: config/SecurityConfig.java --- cadena de filtros Spring Security. | `listados-del-libro/capitulo8/config/SecurityConfig.java` | `backend/src/main/java/ec/uteq/agrocadena/config/SecurityConfig.java` |
| 9 | `lst:agro9-k8s` | Archivo: k8s/backend.yaml --- despliegue y servicio del backend en K3s. | `listados-del-libro/capitulo9/k8s/backend.yaml` | `infra/k8s/backend.yaml` |
| 9 | `lst:agro9-compose` | Archivo: docker-compose.yml --- composición de servicios para desarrollo. | `listados-del-libro/capitulo9/docker-compose.yml` | `infra/docker-compose.yml` |
| 9 | `lst:agro9-docker` | Archivo: agrocadena-backend/Dockerfile --- imagen multi-etapa del backend. | `listados-del-libro/capitulo9/agrocadena-backend/Dockerfile` | `backend/Dockerfile` |
| 9 | `lst:agro9-ci` | Archivo: .github/workflows/ci.yml --- canalización CI/CD/CM. | `listados-del-libro/capitulo9/.github/workflows/ci.yml` | `.github/workflows/ci.yml` |
| 9 | `lst:agro9-actuator` | Archivo: application.yml --- sección de observabilidad. | `listados-del-libro/capitulo9/application.yml` | `backend/src/main/resources/application.yml` |
| 9 | `lst:agro9-metricas` | Archivo: service/MetricasAlerta.java --- medición de la latencia de alerta. | `listados-del-libro/capitulo9/service/MetricasAlerta.java` | `backend/src/main/java/ec/uteq/agrocadena/service/MetricasAlerta.java` |
| 9 | `lst:agro9-reglas` | Archivo: prometheus/reglas-agrocadena.yml --- alertas sobre los SLO. | `listados-del-libro/capitulo9/prometheus/reglas-agrocadena.yml` | `infra/prometheus/reglas-agrocadena.yml` |
| 9 | `lst:agro9-runbook-md` | Archivo: docs/runbook/latencia-alerta.md --- entrada del runbook. | `listados-del-libro/capitulo9/docs/runbook/latencia-alerta.md` | `docs/runbook/latencia-alerta.md` |
| 9 | `lst:agro9-baja` | Archivo: ops/baja-dispositivo.sh --- baja segura de un dispositivo. | `listados-del-libro/capitulo9/ops/baja-dispositivo.sh` | `infra/ops/baja-dispositivo.sh` |
| 9 | `lst:agro9-traza-test` | Archivo: traza/TrazabilidadTest.java --- verificación automática de la matriz. | `listados-del-libro/capitulo9/traza/TrazabilidadTest.java` | `backend/src/test/java/ec/uteq/agrocadena/traza/TrazabilidadTest.java` |

## Diferencias entre los listados impresos y el proyecto ejecutable

Los listados del libro son fragmentos pensados para explicar un concepto. Para que el sistema funcione completo, el proyecto agrega o ajusta lo siguiente:

- **Clases omitidas en el texto.** `Contenedor`, `Dispositivo`, `Alerta`, sus repositorios, `ColaReintento` (cola del *circuit breaker*) y los métodos de acceso de `Lectura`, que el libro omite por brevedad.
- **Consulta para KPI.** `LecturaRepository` incluye `lecturasPorContenedor(codigo, desde)`, que usa `KpiService` (cap. 3).
- **Hipertabla.** TimescaleDB exige que la clave primaria incluya la columna de partición; `db/02-funciones-y-triggers.sql` amplía la clave de `lectura` a `(id, instante)` antes de `create_hypertable`.
- **Formato del mensaje de borde.** El decodificador publica `dispositivo`, `instante`, `temperatura` y `humedad`, que son los campos del contrato del capítulo 5 y del `IngresoDto` del *backend*.
- **Consumidor MQTT.** Usa el servicio resiliente del capítulo 7 y registra el módulo de fechas de Jackson para leer `instante`.
- **TLS en el backend.** `MqttConfig` confía en la CA propia del *broker* (`MQTT_CA_FILE`).
- **Seguridad.** `SecurityConfig` (cap. 8) se activa con el perfil `prod`; el emisor de JWT se configura por variable de entorno y TLS se termina en el Ingress del clúster. `SecurityDevConfig` deja la API abierta solo para desarrollo local.
- **Imagen Docker.** El `Dockerfile` copia el Maven Wrapper (`mvnw`, `.mvn/`), que el listado impreso usa sin copiar.
- **Integración continua.** El flujo compila y prueba los tres componentes (backend, web y móvil) y publica las imágenes en `ghcr.io` al crear una etiqueta `v*`.
- **Mosquitto.** La configuración del laboratorio del capítulo 8 usa certificado de cliente por nodo; la del proyecto usa TLS 1.3 con usuario y contraseña por servicio y una ACL con los tópicos de AgroCadena.

## Licencia

Código publicado con fines educativos junto con la obra. Ver `LICENSE`.
