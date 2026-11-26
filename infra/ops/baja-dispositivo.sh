#!/usr/bin/env bash
# Uso: ./baja-dispositivo.sh EM300-TH-0042
set -euo pipefail
ID="$1"
CA=/opt/agrocadena/ca

# 1. Revocar el certificado del dispositivo y regenerar la CRL
openssl ca -config "$CA/openssl.cnf" -revoke "$CA/certs/$ID.crt"
openssl ca -config "$CA/openssl.cnf" -gencrl -out "$CA/crl/agrocadena.crl"

# 2. Entregar la nueva CRL al broker y reiniciarlo de forma controlada
kubectl -n agrocadena create secret generic mosquitto-crl \
  --from-file=agrocadena.crl="$CA/crl/agrocadena.crl" \
  --dry-run=client -o yaml | kubectl apply -f -
kubectl -n agrocadena rollout restart deploy/mosquitto

# 3. Marcar la baja en la plataforma (el historial se conserva)
curl -fsS -X POST -H "Authorization: Bearer $TOKEN_OPERACION" \
  "https://api.agrocadena.example/api/dispositivos/$ID/baja"

# 4. Dejar evidencia auditable
echo "$(date -Is) baja $ID por $(whoami)" >> /var/log/agrocadena/bajas.log
