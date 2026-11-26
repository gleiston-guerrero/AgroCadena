//src/main/java/ec/uteq/agrocadena/messaging/ConsumidorMqtt.java
package ec.uteq.agrocadena.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import ec.uteq.agrocadena.service.TelemetriaServiceResiliente;
import ec.uteq.agrocadena.web.dto.IngresoDto;
import ec.uteq.agrocadena.traza.Requisito;
import org.slf4j.*;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

@Component
@Requisito({"RF-01","RF-07"})   // ingesta + tolerancia a desconexion
public class ConsumidorMqtt {
	
	private static final Logger log = LoggerFactory.getLogger(ConsumidorMqtt.class);
	private final TelemetriaServiceResiliente servicio;   // cap. 7: con circuit breaker
	private final ObjectMapper mapper = new ObjectMapper().findAndRegisterModules(); // soporte de Instant
	
	public ConsumidorMqtt(TelemetriaServiceResiliente s) { this.servicio = s; }
	
	@ServiceActivator(inputChannel = "mqttEntrada")
	public void recibir(Message<String> mensaje) {
		try {
			IngresoDto dto = mapper.readValue(mensaje.getPayload(), IngresoDto.class);
			servicio.persistir(dto);
		} catch (Exception ex) {
			log.error("Error procesando telemetria", ex);
		}
	}
}
