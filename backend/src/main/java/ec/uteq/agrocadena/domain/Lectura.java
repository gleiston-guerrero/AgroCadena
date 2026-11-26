//src/main/java/ec/uteq/agrocadena/domain/Lectura.java
package ec.uteq.agrocadena.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import ec.uteq.agrocadena.traza.Requisito;
import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "lectura",
indexes = @Index(name="ix_lectura_disp_inst",
columnList = "dispositivo_id, instante"))
@Requisito({"RF-01","RF-04","RF-06"})
public class Lectura {
	
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne(optional = false) @JoinColumn(name = "dispositivo_id")
	private Dispositivo dispositivo;
	
	@NotNull
	private Instant instante;
	
	@DecimalMin("-30.0") @DecimalMax("70.0")
	private BigDecimal temperatura;
	
	@DecimalMin("0.0")   @DecimalMax("100.0")
	private BigDecimal humedad;
	
	private BigDecimal latitud;
	private BigDecimal longitud;
	
	@Column(nullable = false)
	private boolean valida = true;
	
	public Lectura() { }
	
	public Long getId() { return id; }
	public Dispositivo getDispositivo() { return dispositivo; }
	public void setDispositivo(Dispositivo dispositivo) { this.dispositivo = dispositivo; }
	public Instant getInstante() { return instante; }
	public void setInstante(Instant instante) { this.instante = instante; }
	public BigDecimal getTemperatura() { return temperatura; }
	public void setTemperatura(BigDecimal temperatura) { this.temperatura = temperatura; }
	public BigDecimal getHumedad() { return humedad; }
	public void setHumedad(BigDecimal humedad) { this.humedad = humedad; }
	public BigDecimal getLatitud() { return latitud; }
	public void setLatitud(BigDecimal latitud) { this.latitud = latitud; }
	public BigDecimal getLongitud() { return longitud; }
	public void setLongitud(BigDecimal longitud) { this.longitud = longitud; }
	public boolean isValida() { return valida; }
	public void setValida(boolean valida) { this.valida = valida; }
}
