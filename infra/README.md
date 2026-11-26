# Infraestructura

## Desarrollo local con Docker Compose

```bash
cp .env.example .env                 # cambie las contraseñas
./mosquitto/generar-certs.sh         # crea mosquitto/certs/ca.crt, broker.crt y broker.key
./mosquitto/crear-usuarios.sh        # crea mosquitto/passwd (usuarios backend y ug65)
docker compose up -d --build
```

En Windows ejecute los `.sh` desde Git Bash. Luego aplique los scripts de `../db/` (ver `db/README.md`).

| Servicio | Puerto | Descripción |
|---|---|---|
| `postgres` | 5432 | PostgreSQL 16 + TimescaleDB |
| `mosquitto` | 8883 | MQTT solo sobre TLS 1.3, con usuario/contraseña y ACL |
| `backend` | 8080 | API Spring Boot |
| `web` | 8081 | Tablero Vue servido por nginx |

## Kubernetes (K3s)

```bash
kubectl apply -f k8s/00-namespace.yaml
cp k8s/01-secretos.example.yaml k8s/01-secretos.yaml   # edite valores; no lo suba
kubectl apply -f k8s/01-secretos.yaml
kubectl -n agrocadena create secret generic mosquitto-certs \
  --from-file=mosquitto/certs/ca.crt --from-file=mosquitto/certs/broker.crt --from-file=mosquitto/certs/broker.key
kubectl -n agrocadena create secret generic mosquitto-passwd --from-file=mosquitto/passwd
kubectl apply -f k8s/
```

Las imágenes `ghcr.io/gleiston-guerrero/agrocadena/backend` y `.../web` las publica el flujo de CI al crear una etiqueta `v*`.

## Observabilidad y operación

- `prometheus/reglas-agrocadena.yml`: alertas sobre los SLO (cap. 9).
- `ops/baja-dispositivo.sh`: baja segura de un dispositivo (revoca su certificado).
- `../docs/runbook/latencia-alerta.md`: procedimiento ante la alerta `LatenciaAlertaAlta`.
