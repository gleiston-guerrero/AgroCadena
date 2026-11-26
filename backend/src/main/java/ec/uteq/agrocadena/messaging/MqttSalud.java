// backend/src/main/java/ec/uteq/agrocadena/messaging/MqttSalud.java
package ec.uteq.agrocadena.messaging;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.context.event.EventListener;
import org.springframework.integration.mqtt.event.MqttConnectionFailedEvent;
import org.springframework.integration.mqtt.event.MqttSubscribedEvent;
import org.springframework.stereotype.Component;

/**
 * Indicador de salud "mqtt": la sonda readiness del cluster (cap. 9) no
 * envia trafico al pod hasta que el consumidor se suscribe al broker.
 */
@Component("mqtt")
public class MqttSalud implements HealthIndicator {

  private final boolean habilitado;
  private volatile boolean suscrito = false;

  public MqttSalud(@Value("${mqtt.enabled:true}") boolean habilitado) {
    this.habilitado = habilitado;
  }

  @EventListener
  public void alSuscribir(MqttSubscribedEvent e) {
    suscrito = true;
  }

  @EventListener
  public void alPerderConexion(MqttConnectionFailedEvent e) {
    suscrito = false;
  }

  @Override
  public Health health() {
    if (!habilitado) {
      return Health.up().withDetail("broker", "deshabilitado").build();
    }
    return suscrito
      ? Health.up().build()
      : Health.down().withDetail("broker", "sin suscripcion").build();
  }
}
