package ec.uteq.agrocadena.domain;

import jakarta.persistence.*;
import java.time.Instant;

/** Alerta registrada por el trigger trg_alerta_excursion (db/02-funciones-y-triggers.sql). */
@Entity
@Table(name = "alerta")
public class Alerta {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(optional = false) @JoinColumn(name = "dispositivo_id")
	private Dispositivo dispositivo;

	@Column(nullable = false)
	private Instant instante;

	@Column(nullable = false, length = 40)
	private String tipo;

	@Column(length = 255)
	private String detalle;

	public Alerta() { }

	public Long getId() { return id; }
	public Dispositivo getDispositivo() { return dispositivo; }
	public Instant getInstante() { return instante; }
	public String getTipo() { return tipo; }
	public String getDetalle() { return detalle; }
}
