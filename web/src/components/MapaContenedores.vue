<!-- web/src/components/MapaContenedores.vue -->
<script setup>
import { ref, onMounted } from "vue";
import L from "leaflet";
import { ultimasLecturas } from "../services/api.js";

const mapa = ref(null);
const CONTENEDORES = ["CNT-007", "CNT-012"];
const PUERTO_GUAYAQUIL = [-2.28, -79.91]; // centro por defecto

onMounted(async () => {
  const m = L.map(mapa.value).setView(PUERTO_GUAYAQUIL, 7);
  L.tileLayer("https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png", {
    attribution: "&copy; OpenStreetMap",
  }).addTo(m);
  for (const codigo of CONTENEDORES) {
    const ls = await ultimasLecturas(codigo);
    const conGps = ls.find(
      (l) => l.latitud != null && l.longitud != null,
    );
    if (conGps) {
      L.circleMarker([conGps.latitud, conGps.longitud], { radius: 8 })
        .addTo(m)
        .bindPopup(`${codigo}: ${conGps.temperatura} °C`);
    }
  }
});
</script>

<template>
  <section class="panel">
    <h2>Ubicación de contenedores</h2>
    <div ref="mapa" class="mapa"></div>
  </section>
</template>

<style scoped>
.mapa {
  height: 320px;
}
</style>
