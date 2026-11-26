# Tablero web (Vue 3 · Vite)

Tablero del capítulo 3: tarjetas de KPI (MKT, cumplimiento y tiempo fuera de rango), serie de temperatura con Chart.js y mapa de contenedores con Leaflet.

```bash
npm install
npm run dev          # http://localhost:5173 (redirige /v1 al backend en :8080)
npm run build        # genera dist/
```

En producción, `Dockerfile` compila el proyecto y lo sirve con nginx, que redirige `/v1` al servicio `backend`.

Si el *backend* corre con el perfil `prod`, guarde el JWT en `localStorage` con la clave `jwt`; `src/services/api.js` lo agrega a cada petición.
