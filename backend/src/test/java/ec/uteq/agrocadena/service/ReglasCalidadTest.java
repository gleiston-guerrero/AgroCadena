// backend/src/test/java/ec/uteq/agrocadena/service/ReglasCalidadTest.java
package ec.uteq.agrocadena.service;

import static org.junit.jupiter.api.Assertions.*;

import ec.uteq.agrocadena.web.dto.IngresoDto;
import java.math.BigDecimal;
import java.time.Instant;
import org.junit.jupiter.api.Test;

class ReglasCalidadTest {

  @Test
  void rangoValido() {
    IngresoDto d = new IngresoDto(
      "EUI-1",
      Instant.now(),
      new BigDecimal("4.5"),
      new BigDecimal("85"),
      null,
      null
    );
    assertTrue(ReglasCalidad.enRango(d));
  }

  @Test
  void temperaturaFueraDeRangoSeRechaza() {
    IngresoDto d = new IngresoDto(
      "EUI-1",
      Instant.now(),
      new BigDecimal("85"),
      new BigDecimal("50"),
      null,
      null
    );
    assertFalse(ReglasCalidad.enRango(d));
  }

  @Test
  void instanteFuturoSeRechaza() {
    IngresoDto d = new IngresoDto(
      "EUI-1",
      Instant.now().plusSeconds(60),
      new BigDecimal("4"),
      new BigDecimal("80"),
      null,
      null
    );
    assertFalse(ReglasCalidad.instanteCoherente(d, Instant.now()));
  }
}
