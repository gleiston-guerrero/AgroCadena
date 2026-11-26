// backend/src/main/java/ec/uteq/agrocadena/web/controller/EstadoController.java
package ec.uteq.agrocadena.web.controller;

import ec.uteq.agrocadena.domain.Contenedor;
import ec.uteq.agrocadena.persistence.ContenedorRepository;
import ec.uteq.agrocadena.persistence.LecturaRepository;
import ec.uteq.agrocadena.service.DetectorExcursion;
import ec.uteq.agrocadena.service.DetectorExcursion.Muestra;
import ec.uteq.agrocadena.traza.Requisito;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

/** Semaforo del contenedor para el tablero web (HU-03). */
@RestController
@RequestMapping("/v1/contenedores")
@Requisito({ "RF-05" })
public class EstadoController {

  private static final Duration VENTANA = Duration.ofHours(1);
  private static final Duration UMBRAL = Duration.ofMinutes(10);
  private final ContenedorRepository contenedores;
  private final LecturaRepository lecturas;

  public EstadoController(ContenedorRepository c, LecturaRepository l) {
    this.contenedores = c;
    this.lecturas = l;
  }

  @GetMapping("/{codigo}/estado")
  public Map<String, Object> estado(@PathVariable String codigo) {
    Contenedor c = contenedores
      .findByCodigo(codigo)
      .orElseThrow(() ->
        new ResponseStatusException(HttpStatus.NOT_FOUND)
      );
    DetectorExcursion d = new DetectorExcursion(
      c.getTempMin(),
      c.getTempMax(),
      UMBRAL
    );
    List<Muestra> ventana = lecturas
      .lecturasPorContenedor(codigo, Instant.now().minus(VENTANA))
      .stream()
      .map(l -> new Muestra(l.getInstante(), l.getTemperatura()))
      .toList();
    return Map.of(
      "codigo",
      codigo,
      "estado",
      d.estado(ventana).name().toLowerCase(),
      "minutosFuera",
      d.duracionActual(ventana).toMinutes()
    );
  }
}
