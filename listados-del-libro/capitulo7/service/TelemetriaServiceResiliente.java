//src/main/java/ec/uteq/agrocadena/service/TelemetriaServiceResiliente.java
package ec.uteq.agrocadena.service;

import ec.uteq.agrocadena.traza.Requisito;
import ec.uteq.agrocadena.web.dto.IngresoDto;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.stereotype.Component;

@Component
@Requisito({"RF-07","RNF-01"})
public class TelemetriaServiceResiliente {
	
	private final TelemetriaService delegado;
	public TelemetriaServiceResiliente(TelemetriaService d) { this.delegado = d; }
	
	@CircuitBreaker(name = "persistencia", fallbackMethod = "encolar")
	public void persistir(IngresoDto e) { delegado.persistir(e); }
	
	private void encolar(IngresoDto e, Throwable ex) {
		// Encolar en cola interna para reintento posterior.
		ColaReintento.encolar(e);
	}
}
