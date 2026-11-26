// backend/src/main/java/ec/uteq/agrocadena/service/DetectorExcursion.java
package ec.uteq.agrocadena.service;

import ec.uteq.agrocadena.traza.Requisito;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.util.List;

/**
 * Regla de excursion termica sostenida. Aplica la misma logica que la
 * funcion fn_excursion_sostenida de la base de datos (db/02).
 */
@Requisito({ "RF-03" })
public final class DetectorExcursion {

  public enum Estado {
    VERDE,
    AMBAR,
    ROJO,
  }

  public record Muestra(Instant instante, BigDecimal temperatura) {}

  private final BigDecimal tMin;
  private final BigDecimal tMax;
  private final Duration umbral;

  public DetectorExcursion(
    BigDecimal tMin,
    BigDecimal tMax,
    Duration umbral
  ) {
    this.tMin = tMin;
    this.tMax = tMax;
    this.umbral = umbral;
  }

  public boolean fueraDeRango(BigDecimal t) {
    return t.compareTo(tMin) < 0 || t.compareTo(tMax) > 0;
  }

  /** Tiempo que la ultima muestra lleva fuera de rango sin interrupcion. */
  public Duration duracionActual(List<Muestra> ordenadas) {
    Instant inicio = null;
    Instant ultimo = null;
    for (Muestra m : ordenadas) {
      if (fueraDeRango(m.temperatura())) {
        if (inicio == null) inicio = m.instante();
      } else {
        inicio = null;
      }
      ultimo = m.instante();
    }
    return inicio == null
      ? Duration.ZERO
      : Duration.between(inicio, ultimo);
  }

  /** true si en algun momento la excursion alcanzo el umbral. */
  public boolean huboExcursionSostenida(List<Muestra> ordenadas) {
    for (int i = 1; i <= ordenadas.size(); i++) {
      if (
        duracionActual(ordenadas.subList(0, i)).compareTo(umbral) >= 0
      ) {
        return true;
      }
    }
    return false;
  }

  /** Semaforo del tablero: rojo, ambar o verde. */
  public Estado estado(List<Muestra> ordenadas) {
    if (huboExcursionSostenida(ordenadas)) return Estado.ROJO;
    boolean algunaFuera = ordenadas
      .stream()
      .anyMatch(m -> fueraDeRango(m.temperatura()));
    return algunaFuera ? Estado.AMBAR : Estado.VERDE;
  }
}
