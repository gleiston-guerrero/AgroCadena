//src/test/java/ec/uteq/agrocadena/service/ReglasCalidadTest.java
package ec.uteq.agrocadena.service;

import ec.uteq.agrocadena.web.dto.IngresoDto;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal; import java.time.Instant;
import static org.junit.jupiter.api.Assertions.*;

class ReglasCalidadTest {
	
	@Test void rangoValido() {
		IngresoDto d = new IngresoDto("EUI-1", Instant.now(),
		new BigDecimal("4.5"), new BigDecimal("85"), null, null);
		assertTrue(ReglasCalidad.enRango(d));
	}
	
	@Test void temperaturaFueraDeRangoSeRechaza() {
		IngresoDto d = new IngresoDto("EUI-1", Instant.now(),
		new BigDecimal("85"), new BigDecimal("50"), null, null);
		assertFalse(ReglasCalidad.enRango(d));
	}
	
	@Test void instanteFuturoSeRechaza() {
		IngresoDto d = new IngresoDto("EUI-1",
		Instant.now().plusSeconds(60),
		new BigDecimal("4"), new BigDecimal("80"), null, null);
		assertFalse(ReglasCalidad.instanteCoherente(d, Instant.now()));
	}
}
