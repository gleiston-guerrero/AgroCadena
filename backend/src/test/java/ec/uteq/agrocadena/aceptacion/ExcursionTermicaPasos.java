// backend/src/test/java/ec/uteq/agrocadena/aceptacion/ExcursionTermicaPasos.java
package ec.uteq.agrocadena.aceptacion;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import ec.uteq.agrocadena.domain.*;
import ec.uteq.agrocadena.persistence.AlertaRepository;
import ec.uteq.agrocadena.service.*;
import ec.uteq.agrocadena.service.DetectorExcursion.Muestra;
import io.cucumber.java.es.*;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/** Pasos de features/excursion-termica.feature (HU-03). */
public class ExcursionTermicaPasos {

  private static final Duration UMBRAL = Duration.ofMinutes(10);
  private Contenedor contenedor;
  private DetectorExcursion detector;
  private final List<Muestra> lecturas = new ArrayList<>();
  private final List<Alerta> alertas = new ArrayList<>();
  private final List<Alerta> notificadas = new ArrayList<>();

  @Dado(
    "un contenedor {string} con producto {word} y rango [{double}, {double}] degC"
  )
  public void unContenedor(
    String codigo,
    String producto,
    double min,
    double max
  ) {
    contenedor = new Contenedor(
      codigo,
      producto,
      BigDecimal.valueOf(min),
      BigDecimal.valueOf(max)
    );
    detector = new DetectorExcursion(
      contenedor.getTempMin(),
      contenedor.getTempMax(),
      UMBRAL
    );
  }

  @Cuando(
    "recibo lecturas con {double} degC durante {int} minutos consecutivos"
  )
  public void lecturasSostenidas(double temp, int minutos) {
    registrar(temp, minutos);
  }

  @Cuando(
    "recibo lecturas con {double} degC durante {int} minutos y luego vuelve a rango"
  )
  public void lecturasBreves(double temp, int minutos) {
    registrar(temp, minutos);
    registrar(
      contenedor
        .getTempMin()
        .add(contenedor.getTempMax())
        .doubleValue() / 2,
      1
    );
  }

  @Entonces("se debe registrar una alerta de tipo {string}")
  public void seRegistraAlerta(String tipo) {
    assertEquals(1, alertas.size());
    assertEquals(tipo, alertas.get(0).getTipo());
  }

  @Entonces("no se debe registrar alerta critica")
  public void noSeRegistraAlerta() {
    assertTrue(alertas.isEmpty());
  }

  @Y("la app movil del jefe de logistica recibe una notificacion push")
  public void recibeNotificacion() {
    AlertaRepository repo = mock(AlertaRepository.class);
    when(repo.maximoId()).thenReturn(0L);
    when(repo.findByIdGreaterThanOrderByIdAsc(0L)).thenReturn(alertas);
    AlertaService servicio = new AlertaService(
      repo,
      notificadas::add,
      new MetricasAlerta(new SimpleMeterRegistry())
    );
    servicio.despachar();
    assertEquals(alertas, notificadas);
  }

  @Y("el tablero web muestra el contenedor en estado {word}")
  public void estadoTablero(String esperado) {
    String estado = detector.estado(lecturas).name().toLowerCase();
    assertEquals(esperado, estado);
  }

  /** Una lectura por minuto, como el EM300-TH del caso AgroCadena. */
  private void registrar(double temp, int minutos) {
    Instant t = lecturas.isEmpty()
      ? Instant.parse("2026-11-25T08:00:00Z")
      : lecturas
          .get(lecturas.size() - 1)
          .instante()
          .plusSeconds(60);
    for (int i = 0; i < minutos; i++, t = t.plusSeconds(60)) {
      lecturas.add(new Muestra(t, BigDecimal.valueOf(temp)));
      boolean yaAlertado = !alertas.isEmpty();
      if (
        !yaAlertado &&
        detector.fueraDeRango(BigDecimal.valueOf(temp)) &&
        detector.duracionActual(lecturas).compareTo(UMBRAL) >= 0
      ) {
        Dispositivo d = new Dispositivo(
          "24E124136B316001",
          "EM300-TH",
          contenedor
        );
        alertas.add(
          new Alerta(
            d,
            t,
            "excursion_termica",
            "Temperatura fuera de rango sostenida 10 minutos"
          )
        );
      }
    }
  }
}
