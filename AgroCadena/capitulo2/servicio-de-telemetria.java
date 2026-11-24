			
//src/main/java/ec/uteq/agrocadena/service/TelemetriaService.java
package ec.uteq.agrocadena.service;

import ec.uteq.agrocadena.domain.*;
import ec.uteq.agrocadena.persistence.*;
import ec.uteq.agrocadena.web.dto.IngresoDto;
import ec.uteq.agrocadena.traza.Requisito;
import org.slf4j.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.Instant;

@Service
@Requisito({"RF-01","RF-04","RF-06","RF-07"})
public class TelemetriaService {
	
	private static final Logger log = LoggerFactory.getLogger(TelemetriaService.class);
	private final DispositivoRepository dispositivos;
	private final LecturaRepository lecturas;
	
	public TelemetriaService(DispositivoRepository d, LecturaRepository l) {
		this.dispositivos = d; this.lecturas = l;
	}
	
	@Transactional
	public void persistir(IngresoDto e) {
		if (!ReglasCalidad.enRango(e) ||
		!ReglasCalidad.instanteCoherente(e, Instant.now())) {
			log.warn("Lectura descartada: {}", e);
			return;
		}
		Dispositivo dev = dispositivos.findByDevEui(e.dispositivo())
		.orElseThrow(() ->
		new IllegalStateException("Dispositivo no registrado"));
		Lectura l = new Lectura();
		l.setDispositivo(dev);
		l.setInstante(e.instante());
		l.setTemperatura(e.temperatura());
		l.setHumedad(e.humedad());
		l.setLatitud(e.latitud());
		l.setLongitud(e.longitud());
		lecturas.save(l);
	}
}
