<!-- src/components/GraficoLecturas.vue: serie de temperatura con Chart.js -->
<script setup>
	import { ref, onMounted, onUnmounted } from "vue";
	import { Chart, LineController, LineElement, PointElement, LinearScale,
		CategoryScale, Tooltip, Legend } from "chart.js";
	import { ultimasLecturas } from "../services/api.js";

	Chart.register(LineController, LineElement, PointElement, LinearScale,
		CategoryScale, Tooltip, Legend);

	const props = defineProps({ codigo: String });
	const lienzo = ref(null);
	let grafico, timer;

	const cargar = async () => {
		const datos = (await ultimasLecturas(props.codigo)).slice().reverse();
		grafico.data.labels = datos.map(l => new Date(l.instante).toLocaleTimeString());
		grafico.data.datasets[0].data = datos.map(l => l.temperatura);
		grafico.update();
	};

	onMounted(() => {
		grafico = new Chart(lienzo.value, {
			type: "line",
			data: { labels: [], datasets: [{ label: "Temperatura (°C)", data: [],
				borderColor: "#2e6da4", tension: 0.2 }] },
			options: { responsive: true, animation: false }
		});
		cargar();
		timer = setInterval(cargar, 30000);
	});
	onUnmounted(() => { clearInterval(timer); grafico?.destroy(); });
</script>

<template>
	<section class="panel"><h2>Temperatura · {{ codigo }}</h2><canvas ref="lienzo"></canvas></section>
</template>
