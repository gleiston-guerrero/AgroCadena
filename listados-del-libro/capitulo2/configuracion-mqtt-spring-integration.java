//src/main/java/ec/uteq/agrocadena/config/MqttConfig.java
package ec.uteq.agrocadena.config;

import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.mqtt.core.*;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.messaging.MessageChannel;

@Configuration @IntegrationComponentScan
public class MqttConfig {
	
	@Value("${mqtt.broker}")        String broker;
	@Value("${mqtt.client-id}")     String clientId;
	@Value("${mqtt.topic-lectura}") String topic;
	@Value("${mqtt.username}")      String user;
	@Value("${mqtt.password}")      String pass;
	
	@Bean MessageChannel mqttEntrada() { return new DirectChannel(); }
	
	@Bean MqttPahoClientFactory factory() {
		DefaultMqttPahoClientFactory f = new DefaultMqttPahoClientFactory();
		MqttConnectOptions o = new MqttConnectOptions();
		o.setServerURIs(new String[]{broker});
		o.setUserName(user); o.setPassword(pass.toCharArray());
		o.setAutomaticReconnect(true);
		f.setConnectionOptions(o);
		return f;
	}
	
	@Bean MqttPahoMessageDrivenChannelAdapter adaptador() {
		MqttPahoMessageDrivenChannelAdapter a =
		new MqttPahoMessageDrivenChannelAdapter(clientId, factory(), topic);
		a.setQos(1);
		a.setOutputChannel(mqttEntrada());
		return a;
	}
}
