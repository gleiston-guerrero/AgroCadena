// backend/src/main/java/ec/uteq/agrocadena/web/controller/ContenedorController.java
package ec.uteq.agrocadena.web.controller;

import ec.uteq.agrocadena.persistence.LecturaRepository;
import ec.uteq.agrocadena.traza.Requisito;
import ec.uteq.agrocadena.web.dto.LecturaDto;
import java.util.List;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/contenedores")
@Requisito({ "RF-05", "RF-06", "RNF-08" })
public class ContenedorController {

  private final LecturaRepository repositorio;

  public ContenedorController(LecturaRepository r) {
    this.repositorio = r;
  }

  @GetMapping("/{codigo}/lecturas")
  public List<LecturaDto> ultimas(
    @PathVariable String codigo,
    @RequestParam(defaultValue = "20") int limite
  ) {
    return repositorio
      .ultimasPorContenedor(codigo, PageRequest.of(0, limite))
      .stream()
      .map(LecturaDto::de)
      .toList();
  }
}
