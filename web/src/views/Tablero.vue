<!-- src/views/Tablero.vue -->
<script setup>
	import { ref, onMounted, onUnmounted } from "vue";
	import KpiCard           from "../components/KpiCard.vue";
	import MapaContenedores  from "../components/MapaContenedores.vue";
	import GraficoLecturas   from "../components/GraficoLecturas.vue";
	import { obtenerKpi }    from "../services/api.js";
	
	const kpi = ref({});
	let timer;
	
	const recargar = async () => {
		kpi.value = await obtenerKpi("CNT-007");
	};
	
	onMounted(()  => { recargar(); timer = setInterval(recargar, 30000); });
	onUnmounted(() => clearInterval(timer));
</script>

<template>
	<section class="kpis">
		<KpiCard etiqueta="MKT"          :valor="kpi.mkt"
		unidad="degC" :umbral="8"  regla="menor" />
		<KpiCard etiqueta="Cumplimiento" :valor="kpi.cumplimiento"
		unidad="%"    :umbral="98" regla="mayor" />
		<KpiCard etiqueta="Tiempo fuera" :valor="kpi.tor_min"
		unidad="min"  :umbral="30" regla="menor" />
	</section>
	<GraficoLecturas codigo="CNT-007" />
	<MapaContenedores />
</template>
