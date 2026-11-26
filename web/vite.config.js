// web/vite.config.js
import { defineConfig } from "vite";
import vue from "@vitejs/plugin-vue";

// En desarrollo, /v1 se redirige al backend Spring Boot (puerto 8080).
export default defineConfig({
  plugins: [vue()],
  server: {
    port: 5173,
    proxy: { "/v1": "http://localhost:8080" },
  },
  preview: {
    proxy: { "/v1": "http://localhost:8080" },
  },
});
