# LatenciaAlertaAlta - p95 > 30 s (severidad: critica)

## Impacto
Las alertas de excursion termica llegan tarde a los responsables.

## Diagnostico (5 minutos)
1. Grafana, tablero "AgroCadena / Servicio": retraso de la cola MQTT.
2. kubectl -n agrocadena get pods -l app=backend
3. kubectl -n agrocadena logs deploy/backend --since=15m | grep -i circuit

## Accion
- Cola creciente y CPU saturada:
    kubectl -n agrocadena scale deploy/backend --replicas=4
- Circuit breaker "persistencia" abierto: revisar PostgreSQL
  (disco, conexiones, failover); las lecturas quedan encoladas.
- Empezo tras un despliegue:
    kubectl -n agrocadena rollout undo deploy/backend

## Verificacion
p95 <= 30 s durante 15 minutos seguidos; cerrar el incidente.

## Escalamiento
Sin mejora en 30 minutos: avisar al responsable de plataforma.
