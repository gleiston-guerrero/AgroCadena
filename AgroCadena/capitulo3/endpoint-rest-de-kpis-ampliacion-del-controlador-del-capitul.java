// src/main/java/ec/uteq/agrocadena/web/controller/KpiController.java
package ec.uteq.agrocadena.web.controller;

import ec.uteq.agrocadena.service.KpiService;
import ec.uteq.agrocadena.traza.Requisito;
import org.springframework.web.bind.annotation.*;
import java.time.Duration;
import java.util.Map;

@RestController
@RequestMapping("/v1/contenedores/{codigo}/kpi")
@Requisito({"RF-05","RNF-08"})
public class KpiController {
	
	private final KpiService kpis;
	public KpiController(KpiService k) { this.kpis = k; }
	
	@GetMapping
	public Map<String,Object> resumen(@PathVariable String codigo,
	@RequestParam(defaultValue = "PT24H") String ventana) {
		Duration v = Duration.parse(ventana);
		return Map.of(
		"mkt",           kpis.mkt(codigo, v),
		"tor_min",       kpis.tor(codigo, v, 5),
		"cumplimiento",  kpis.cumplimiento(codigo, v)
		);
	}
}
