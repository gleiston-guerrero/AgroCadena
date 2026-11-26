#!/usr/bin/env bash
# infra/mosquitto/generar-certs.sh
# Genera una CA propia y el certificado del broker para desarrollo.
# Uso (Git Bash en Windows, o Linux/macOS):  ./generar-certs.sh [nombre-host]
set -euo pipefail
HOST="${1:-localhost}"
cd "$(dirname "$0")/certs"
openssl req -x509 -newkey rsa:3072 -nodes -days 825 \
  -keyout ca.key -out ca.crt -subj "/CN=AgroCadena CA"
openssl req -newkey rsa:3072 -nodes -keyout broker.key -out broker.csr \
  -subj "/CN=${HOST}"
printf "subjectAltName=DNS:%s,DNS:mosquitto,DNS:localhost,IP:127.0.0.1\n" "$HOST" > san.ext
openssl x509 -req -in broker.csr -CA ca.crt -CAkey ca.key -CAcreateserial \
  -out broker.crt -days 825 -extfile san.ext
rm -f broker.csr san.ext
chmod 644 ca.crt broker.crt broker.key
echo "Certificados creados en $(pwd)"
