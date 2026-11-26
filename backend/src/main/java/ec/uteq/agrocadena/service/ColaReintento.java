// backend/src/main/java/ec/uteq/agrocadena/service/ColaReintento.java
package ec.uteq.agrocadena.service;

import ec.uteq.agrocadena.traza.Requisito;
import ec.uteq.agrocadena.web.dto.IngresoDto;
import java.util.concurrent.ConcurrentLinkedQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Cola interna de reintento usada por el fallback del circuit breaker (cap. 7).
 * Cuando la persistencia se recupera, reinyecta las lecturas encoladas.
 */
@Component
@Requisito({ "RF-07" })
public class ColaReintento {

  private static final Logger log = LoggerFactory.getLogger(
    ColaReintento.class
  );
  private static final ConcurrentLinkedQueue<IngresoDto> COLA =
    new ConcurrentLinkedQueue<>();
  private static final int CAPACIDAD = 10_000;

  private final TelemetriaService servicio;

  public ColaReintento(TelemetriaService servicio) {
    this.servicio = servicio;
  }

  public static void encolar(IngresoDto e) {
    if (COLA.size() >= CAPACIDAD) {
      COLA.poll(); // descarta la lectura mas antigua
    }
    COLA.add(e);
  }

  public static int pendientes() {
    return COLA.size();
  }

  @Scheduled(fixedDelayString = "${agrocadena.reintento-ms:30000}")
  public void reintentar() {
    int n = COLA.size();
    for (int i = 0; i < n; i++) {
      IngresoDto e = COLA.poll();
      if (e == null) return;
      try {
        servicio.persistir(e);
      } catch (IllegalStateException ex) {
        log.warn(
          "Lectura descartada en reintento: {}",
          ex.getMessage()
        );
      } catch (RuntimeException ex) {
        COLA.add(e);
        log.warn(
          "Persistencia aun no disponible; {} lecturas en cola",
          COLA.size()
        );
        return;
      }
    }
  }
}
