// src/services/api.js
import axios from "axios";

export const api = axios.create({
	baseURL: "/v1",
	headers: {
		Authorization: `Bearer ${localStorage.getItem("jwt")}`
	}
});

export const obtenerKpi = (codigo) =>
	api.get(`/contenedores/${codigo}/kpi?ventana=PT24H`).then(r => r.data);

export const ultimasLecturas = (codigo) =>
	api.get(`/contenedores/${codigo}/lecturas?limite=288`).then(r => r.data);
