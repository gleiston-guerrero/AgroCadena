# Backend (Spring Boot 3.3 · Java 21)

API REST y consumidor MQTT de AgroCadena. Paquete raíz `ec.uteq.agrocadena`.

## Compilar y probar

```bash
./mvnw verify          # Windows: mvnw.cmd verify
```

Ejecuta las pruebas `ReglasCalidadTest` (cap. 2) y `TrazabilidadTest` (cap. 9), que comprueba que cada requisito de `src/test/resources/requisitos-backend.txt` tenga código anotado con `@Requisito`.

## Ejecutar

Necesita PostgreSQL con TimescaleDB y el *broker* Mosquitto (los levanta `infra/docker-compose.yml`).

```bash
# PowerShell
$env:DB_PASSWORD="..."; $env:MQTT_PASSWORD="..."; $env:MQTT_CA_FILE="..\infra\mosquitto\certs\ca.crt"
./mvnw spring-boot:run
```

| Variable | Valor por defecto | Uso |
|---|---|---|
| `DB_URL` | `jdbc:postgresql://localhost:5432/agrocadena` | Base de datos |
| `DB_PASSWORD` | — | Contraseña del usuario `agrocadena` |
| `MQTT_BROKER` | `ssl://localhost:8883` | *Broker* MQTT (solo TLS) |
| `MQTT_PASSWORD` | — | Contraseña del usuario MQTT `backend` |
| `MQTT_CA_FILE` | vacío | CA del *broker* (`infra/mosquitto/certs/ca.crt`) |
| `MQTT_ENABLED` | `true` | `false` para arrancar sin *broker* |
| `SPRING_PROFILES_ACTIVE` | vacío | `prod` activa la seguridad JWT del cap. 8 |
| `JWT_ISSUER` | `https://auth.agrocadena.ec` | Emisor de tokens (perfil `prod`) |

## Endpoints

| Método | Ruta | Capítulo |
|---|---|---|
| GET | `/v1/contenedores/{codigo}/lecturas?limite=20` | 2 |
| GET | `/v1/contenedores/{codigo}/kpi?ventana=PT24H` | 3 |
| GET | `/actuator/health`, `/actuator/prometheus` | 9 |
| GET | `/swagger-ui.html` | 2 |

## Imagen Docker

```bash
docker build -t agrocadena-backend .
```
