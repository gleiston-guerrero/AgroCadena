<!-- web/src/components/KpiCard.vue -->
<script setup>
import { computed } from "vue";
const props = defineProps({
  etiqueta: String,
  valor: [Number, String],
  unidad: String,
  umbral: Number,
  regla: { type: String, default: "menor" }, // "menor": bueno si valor <= umbral
});
const numero = computed(() => Number(props.valor));
const conDato = computed(
  () => props.valor !== undefined && !Number.isNaN(numero.value),
);
const cumple = computed(() =>
  props.regla === "menor"
    ? numero.value <= props.umbral
    : numero.value >= props.umbral,
);
</script>

<template>
  <div
    class="panel kpi"
    :class="{ ok: conDato && cumple, alerta: conDato && !cumple }"
  >
    <span class="etiqueta">{{ etiqueta }}</span>
    <strong>{{ conDato ? numero.toFixed(1) : "—" }}</strong>
    <span class="unidad">{{ unidad }} (umbral {{ umbral }})</span>
  </div>
</template>

<style scoped>
.kpi {
  display: flex;
  flex-direction: column;
  border-left: 6px solid #9aa5b1;
}
.kpi strong {
  font-size: 2rem;
}
.ok {
  border-left-color: #1b7f6b;
}
.alerta {
  border-left-color: #c0392b;
}
.etiqueta,
.unidad {
  color: #52606d;
  font-size: 0.9rem;
}
</style>
