package ec.uteq.agrocadena.domain;

import jakarta.persistence.*;
import java.math.BigDecimal;

/** Contenedor refrigerado que transporta banano o cacao. */
@Entity
@Table(name = "contenedor")
public class Contenedor {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, unique = true, length = 20)
	private String codigo;          // p. ej. CNT-007

	@Column(nullable = false, length = 30)
	private String producto;        // banano | cacao

	@Column(name = "temp_min", nullable = false)
	private BigDecimal tempMin;

	@Column(name = "temp_max", nullable = false)
	private BigDecimal tempMax;

	public Contenedor() { }

	public Long getId() { return id; }
	public String getCodigo() { return codigo; }
	public void setCodigo(String codigo) { this.codigo = codigo; }
	public String getProducto() { return producto; }
	public void setProducto(String producto) { this.producto = producto; }
	public BigDecimal getTempMin() { return tempMin; }
	public void setTempMin(BigDecimal tempMin) { this.tempMin = tempMin; }
	public BigDecimal getTempMax() { return tempMax; }
	public void setTempMax(BigDecimal tempMax) { this.tempMax = tempMax; }
}
