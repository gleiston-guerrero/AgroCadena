// backend/src/main/java/ec/uteq/agrocadena/config/MqttConfig.java
package ec.uteq.agrocadena.config;

import java.io.FileInputStream;
import java.security.KeyStore;
import java.security.cert.CertificateFactory;
import javax.net.ssl.*;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.*;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.mqtt.core.*;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.messaging.MessageChannel;

@Configuration
@IntegrationComponentScan
@ConditionalOnProperty(
  name = "mqtt.enabled",
  havingValue = "true",
  matchIfMissing = true
)
public class MqttConfig {

  @Value("${mqtt.broker}")
  String broker;

  @Value("${mqtt.client-id}")
  String clientId;

  @Value("${mqtt.topic-lectura}")
  String topic;

  @Value("${mqtt.username}")
  String user;

  @Value("${mqtt.password}")
  String pass;

  @Value("${mqtt.ca-file:}")
  String caFile; // CA propia del broker (PEM)

  @Bean
  MessageChannel mqttEntrada() {
    return new DirectChannel();
  }

  @Bean
  MqttPahoClientFactory factory() {
    DefaultMqttPahoClientFactory f = new DefaultMqttPahoClientFactory();
    MqttConnectOptions o = new MqttConnectOptions();
    o.setServerURIs(new String[] { broker });
    o.setUserName(user);
    o.setPassword(pass.toCharArray());
    o.setAutomaticReconnect(true);
    o.setCleanSession(false); // el broker guarda los mensajes QoS 1 pendientes
    if (!caFile.isBlank()) o.setSocketFactory(socketFactory(caFile));
    f.setConnectionOptions(o);
    return f;
  }

  @Bean
  MqttPahoMessageDrivenChannelAdapter adaptador() {
    MqttPahoMessageDrivenChannelAdapter a =
      new MqttPahoMessageDrivenChannelAdapter(
        clientId,
        factory(),
        topic
      );
    a.setQos(1);
    a.setOutputChannel(mqttEntrada());
    return a;
  }

  /** Confia unicamente en la CA del broker (MQTT sobre TLS, cap. 8). */
  @ec.uteq.agrocadena.traza.Requisito({ "RNF-03" })
  static SSLSocketFactory socketFactory(String ca) {
    try (FileInputStream in = new FileInputStream(ca)) {
      KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
      ks.load(null, null);
      ks.setCertificateEntry(
        "agrocadena-ca",
        CertificateFactory.getInstance("X.509").generateCertificate(in)
      );
      TrustManagerFactory tmf = TrustManagerFactory.getInstance(
        TrustManagerFactory.getDefaultAlgorithm()
      );
      tmf.init(ks);
      SSLContext ctx = SSLContext.getInstance("TLSv1.3");
      ctx.init(null, tmf.getTrustManagers(), null);
      return ctx.getSocketFactory();
    } catch (Exception ex) {
      throw new IllegalStateException(
        "No se pudo cargar la CA MQTT: " + ca,
        ex
      );
    }
  }
}
