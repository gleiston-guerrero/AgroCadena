// backend/src/main/java/ec/uteq/agrocadena/config/NotificacionConfig.java
package ec.uteq.agrocadena.config;

import ec.uteq.agrocadena.service.Notificador;
import ec.uteq.agrocadena.service.NotificadorFcm;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Usa Firebase Cloud Messaging cuando se configura la cuenta de servicio
 * (agrocadena.fcm.credenciales); si no, las alertas solo quedan en el log.
 */
@Configuration
public class NotificacionConfig {

  private static final Logger log = LoggerFactory.getLogger("alertas");

  @Bean
  Notificador notificador(
    @Value("${agrocadena.fcm.credenciales:}") String ruta
  ) throws IOException {
    if (!ruta.isBlank()) {
      return new NotificadorFcm(ruta);
    }
    return a ->
      log.warn(
        "ALERTA {} en {}: {}",
        a.getTipo(),
        a.getDispositivo().getContenedor().getCodigo(),
        a.getDetalle()
      );
  }
}
