// web/src/services/api.js
import axios from "axios";

export const api = axios.create({ baseURL: "/v1" });

// El JWT se agrega solo si existe (en el perfil de desarrollo la API no lo exige).
api.interceptors.request.use((cfg) => {
  const jwt = localStorage.getItem("jwt");
  if (jwt) cfg.headers.Authorization = `Bearer ${jwt}`;
  return cfg;
});

export const obtenerKpi = (codigo) =>
  api
    .get(`/contenedores/${codigo}/kpi?ventana=PT24H`)
    .then((r) => r.data);

export const ultimasLecturas = (codigo) =>
  api
    .get(`/contenedores/${codigo}/lecturas?limite=288`)
    .then((r) => r.data);

export const obtenerEstado = (codigo) =>
  api.get(`/contenedores/${codigo}/estado`).then((r) => r.data);
