<!-- web/src/components/EstadoContenedor.vue -->
<!-- Semaforo del contenedor: verde, ambar o rojo (HU-03, cap. 6) -->
<script setup>
import { ref, onMounted, onUnmounted } from "vue";
import { obtenerEstado } from "../services/api.js";

const props = defineProps({ codigo: String });
const estado = ref({ estado: "verde", minutosFuera: 0 });
let timer;

const cargar = async () => {
  estado.value = await obtenerEstado(props.codigo);
};

onMounted(() => {
  cargar();
  timer = setInterval(cargar, 30000);
});
onUnmounted(() => clearInterval(timer));
</script>

<template>
  <section class="panel estado" :class="estado.estado">
    <strong>{{ codigo }}</strong>
    <span>Estado: {{ estado.estado }}</span>
    <span v-if="estado.minutosFuera > 0">
      {{ estado.minutosFuera }} min fuera de rango
    </span>
  </section>
</template>

<style scoped>
.estado {
  display: flex;
  gap: 1.5rem;
  align-items: center;
  border-left: 8px solid #1b7f6b;
}
.ambar {
  border-left-color: #e0a100;
}
.rojo {
  border-left-color: #c0392b;
}
</style>
