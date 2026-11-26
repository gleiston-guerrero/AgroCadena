#!/usr/bin/env bash
# Crea el archivo de contrasenas del broker (usuarios backend y ug65)
# con las contrasenas definidas en ../.env (MQTT_PASSWORD y MQTT_PASSWORD_UG65).
set -euo pipefail
cd "$(dirname "$0")"
set -a; source ../.env; set +a
: > passwd
docker run --rm -v "$(pwd):/w" eclipse-mosquitto:2 sh -c "
  chmod 0700 /w/passwd &&
  mosquitto_passwd -b /w/passwd backend '${MQTT_PASSWORD}' &&
  mosquitto_passwd -b /w/passwd ug65 '${MQTT_PASSWORD_UG65}' &&
  chown 1883:1883 /w/passwd"
echo "Archivo passwd creado."
