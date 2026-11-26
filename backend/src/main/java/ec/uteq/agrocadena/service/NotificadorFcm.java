// backend/src/main/java/ec/uteq/agrocadena/service/NotificadorFcm.java
package ec.uteq.agrocadena.service;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import ec.uteq.agrocadena.domain.Alerta;
import ec.uteq.agrocadena.traza.Requisito;
import java.io.FileInputStream;
import java.io.IOException;

/** Envia la alerta por Firebase Cloud Messaging al topico "alertas". */
@Requisito({ "RF-03" })
public class NotificadorFcm implements Notificador {

  public static final String TOPICO = "alertas";
  private final FirebaseMessaging fcm;

  public NotificadorFcm(String rutaCredenciales) throws IOException {
    try (FileInputStream in = new FileInputStream(rutaCredenciales)) {
      FirebaseOptions opciones = FirebaseOptions.builder()
        .setCredentials(GoogleCredentials.fromStream(in))
        .build();
      this.fcm = FirebaseMessaging.getInstance(
        FirebaseApp.initializeApp(opciones)
      );
    }
  }

  @Override
  public void enviar(Alerta a) {
    String contenedor = a.getDispositivo().getContenedor().getCodigo();
    Message m = Message.builder()
      .setTopic(TOPICO)
      .setNotification(
        Notification.builder()
          .setTitle("Alerta en " + contenedor)
          .setBody(a.getDetalle())
          .build()
      )
      .putData("contenedor", contenedor)
      .putData("tipo", a.getTipo())
      .build();
    try {
      fcm.send(m);
    } catch (Exception ex) {
      throw new IllegalStateException("FCM no confirmo el envio", ex);
    }
  }
}
