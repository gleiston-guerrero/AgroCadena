// backend/src/main/java/ec/uteq/agrocadena/service/KpiService.java
package ec.uteq.agrocadena.service;

import ec.uteq.agrocadena.domain.Lectura;
import ec.uteq.agrocadena.persistence.LecturaRepository;
import ec.uteq.agrocadena.traza.Requisito;
import java.math.BigDecimal;
import java.time.*;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
@Requisito({ "RF-05", "RF-06", "RNF-02" })
public class KpiService {

  private static final double DELTA_H = 83_144.0; // J/mol
  private static final double R = 8.314; // J/(mol*K)
  private static final BigDecimal T_MIN = new BigDecimal("-1");
  private static final BigDecimal T_MAX = new BigDecimal("8");

  private final LecturaRepository lecturas;

  public KpiService(LecturaRepository l) {
    this.lecturas = l;
  }

  /** Temperatura Cinetica Media (USP 1079.2). */
  public double mkt(String codigoContenedor, Duration ventana) {
    List<Lectura> ls = lecturas.lecturasPorContenedor(
      codigoContenedor,
      Instant.now().minus(ventana)
    );
    if (ls.isEmpty()) return Double.NaN;
    double suma = ls
      .stream()
      .mapToDouble(l ->
        Math.exp(
          -DELTA_H / (R * (l.getTemperatura().doubleValue() + 273.15))
        )
      )
      .sum();
    double tK = DELTA_H / R / -Math.log(suma / ls.size());
    return tK - 273.15;
  }

  /** Tiempo fuera de rango, en minutos. */
  public long tor(String codigo, Duration ventana, int periodoMin) {
    return (
      lecturas
        .lecturasPorContenedor(codigo, Instant.now().minus(ventana))
        .stream()
        .filter(this::fueraDeRango)
        .count() * periodoMin
    );
  }

  /** Tasa de cumplimiento termico (%). */
  public double cumplimiento(String codigo, Duration ventana) {
    List<Lectura> ls = lecturas.lecturasPorContenedor(
      codigo,
      Instant.now().minus(ventana)
    );
    if (ls.isEmpty()) return 100.0;
    long dentro = ls
      .stream()
      .filter(l -> !fueraDeRango(l))
      .count();
    return (100.0 * dentro) / ls.size();
  }

  private boolean fueraDeRango(Lectura l) {
    return (
      l.getTemperatura().compareTo(T_MIN) < 0 ||
      l.getTemperatura().compareTo(T_MAX) > 0
    );
  }
}
