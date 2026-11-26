// src/main/java/ec/uteq/agrocadena/service/ReglasCalidad.java
package ec.uteq.agrocadena.service;
import ec.uteq.agrocadena.web.dto.IngresoDto;
import ec.uteq.agrocadena.traza.Requisito;
import java.math.BigDecimal;
import java.time.Instant;

@Requisito({"RF-03","RNF-04"})
public final class ReglasCalidad {
	
	private static final BigDecimal T_MIN = new BigDecimal("-30");
	private static final BigDecimal T_MAX = new BigDecimal("70");
	
	private ReglasCalidad() { }
	
	public static boolean enRango(IngresoDto d) {
		return d.temperatura()!=null
		&& d.temperatura().compareTo(T_MIN) >= 0
		&& d.temperatura().compareTo(T_MAX) <= 0
		&& d.humedad()!=null
		&& d.humedad().compareTo(BigDecimal.ZERO) >= 0
		&& d.humedad().compareTo(new BigDecimal("100")) <= 0;
	}
	
	public static boolean instanteCoherente(IngresoDto d, Instant ahora) {
		return d.instante()!=null && !d.instante().isAfter(ahora);
	}
}
