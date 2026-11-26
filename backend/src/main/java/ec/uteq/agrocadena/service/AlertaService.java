// backend/src/main/java/ec/uteq/agrocadena/service/AlertaService.java
package ec.uteq.agrocadena.service;

import ec.uteq.agrocadena.domain.Alerta;
import ec.uteq.agrocadena.persistence.AlertaRepository;
import ec.uteq.agrocadena.traza.Requisito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Despacha las alertas que registra el trigger tg_alerta_excursion (db/02):
 * las envia a la app movil y mide la latencia de extremo a extremo.
 */
@Service
@Requisito({ "RF-03", "RNF-02" })
public class AlertaService {

  private static final Logger log = LoggerFactory.getLogger(
    AlertaService.class
  );
  private final AlertaRepository alertas;
  private final Notificador notificador;
  private final MetricasAlerta metricas;
  private long ultimoId;

  public AlertaService(
    AlertaRepository alertas,
    Notificador notificador,
    MetricasAlerta metricas
  ) {
    this.alertas = alertas;
    this.notificador = notificador;
    this.metricas = metricas;
    this.ultimoId = alertas.maximoId(); // no reenviar alertas antiguas
  }

  @Scheduled(fixedDelayString = "${agrocadena.alertas-ms:5000}")
  @Transactional(readOnly = true)
  public void despachar() {
    for (Alerta a : alertas.findByIdGreaterThanOrderByIdAsc(ultimoId)) {
      try {
        notificador.enviar(a);
        metricas.registrar(a.getInstante());
      } catch (RuntimeException ex) {
        log.error("No se pudo notificar la alerta {}", a.getId(), ex);
      }
      if (a.getId() != null) ultimoId = a.getId();
    }
  }
}
