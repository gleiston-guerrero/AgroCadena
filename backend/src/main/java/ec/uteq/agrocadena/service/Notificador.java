// backend/src/main/java/ec/uteq/agrocadena/service/Notificador.java
package ec.uteq.agrocadena.service;

import ec.uteq.agrocadena.domain.Alerta;

/** Canal por el que se avisa una alerta a la app movil. */
public interface Notificador {
  void enviar(Alerta alerta);
}
