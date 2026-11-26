// backend/src/main/java/ec/uteq/agrocadena/web/dto/LecturaDto.java
package ec.uteq.agrocadena.web.dto;

import ec.uteq.agrocadena.domain.Lectura;
import java.math.BigDecimal;
import java.time.Instant;

public record LecturaDto(
  Long id,
  Instant instante,
  BigDecimal temperatura,
  BigDecimal humedad,
  BigDecimal latitud,
  BigDecimal longitud
) {
  public static LecturaDto de(Lectura l) {
    return new LecturaDto(
      l.getId(),
      l.getInstante(),
      l.getTemperatura(),
      l.getHumedad(),
      l.getLatitud(),
      l.getLongitud()
    );
  }
}
