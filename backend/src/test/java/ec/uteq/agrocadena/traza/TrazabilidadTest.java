// backend/src/test/java/ec/uteq/agrocadena/traza/TrazabilidadTest.java
package ec.uteq.agrocadena.traza;

import static org.assertj.core.api.Assertions.assertThat;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;

class TrazabilidadTest {

  @Test
  void todoRequisitoDelBackendTieneCodigoAnotado() throws Exception {
    var escaner = new ClassPathScanningCandidateComponentProvider(
      false
    );
    escaner.addIncludeFilter((lector, fabrica) -> true);

    Set<String> cubiertos = new TreeSet<>();
    for (var bd : escaner.findCandidateComponents(
      "ec.uteq.agrocadena"
    )) {
      Class<?> clase = Class.forName(bd.getBeanClassName());
      agregar(clase.getAnnotation(Requisito.class), cubiertos);
      for (var metodo : clase.getDeclaredMethods()) {
        agregar(metodo.getAnnotation(Requisito.class), cubiertos);
      }
    }
    List<String> esperados = Files.readAllLines(
      Path.of("src/test/resources/requisitos-backend.txt")
    );
    assertThat(cubiertos).containsAll(esperados);
  }

  private static void agregar(Requisito r, Set<String> destino) {
    if (r != null) destino.addAll(List.of(r.value()));
  }
}
