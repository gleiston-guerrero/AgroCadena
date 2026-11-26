// backend/src/main/java/ec/uteq/agrocadena/web/dto/IngresoDto.java
package ec.uteq.agrocadena.web.dto;

import java.math.BigDecimal;
import java.time.Instant;

public record IngresoDto(
  String dispositivo,
  Instant instante,
  BigDecimal temperatura,
  BigDecimal humedad,
  BigDecimal latitud,
  BigDecimal longitud
) {}
