// backend/src/main/java/ec/uteq/agrocadena/service/MetricasAlerta.java
package ec.uteq.agrocadena.service;

import ec.uteq.agrocadena.traza.Requisito;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import java.time.Duration;
import java.time.Instant;
import org.springframework.stereotype.Component;

@Component
public class MetricasAlerta {

  private final Timer latencia;

  public MetricasAlerta(MeterRegistry registro) {
    this.latencia = Timer.builder("agrocadena.alerta.latencia")
      .description("Lectura en campo hasta envio de la alerta")
      .register(registro);
  }

  /** Se invoca cuando FCM confirma el envio de una alerta. */
  @Requisito("RNF-02")
  public void registrar(Instant instanteLectura) {
    latencia.record(Duration.between(instanteLectura, Instant.now()));
  }
}
