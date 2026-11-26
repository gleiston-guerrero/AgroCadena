// backend/src/main/java/ec/uteq/agrocadena/domain/Dispositivo.java
package ec.uteq.agrocadena.domain;

import jakarta.persistence.*;

/** Sensor o rastreador instalado en un contenedor (EM300-TH, WS301, FMC130). */
@Entity
@Table(name = "dispositivo")
public class Dispositivo {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(
    name = "dev_eui",
    nullable = false,
    unique = true,
    length = 16
  )
  private String devEui;

  @Column(nullable = false, length = 30)
  private String modelo;

  @Column(nullable = false)
  private boolean activo = true;

  @ManyToOne(optional = false)
  @JoinColumn(name = "contenedor_id")
  private Contenedor contenedor;

  public Dispositivo() {}

  public Dispositivo(
    String devEui,
    String modelo,
    Contenedor contenedor
  ) {
    this.devEui = devEui;
    this.modelo = modelo;
    this.contenedor = contenedor;
  }

  public Long getId() {
    return id;
  }

  public String getDevEui() {
    return devEui;
  }

  public void setDevEui(String devEui) {
    this.devEui = devEui;
  }

  public String getModelo() {
    return modelo;
  }

  public void setModelo(String modelo) {
    this.modelo = modelo;
  }

  public boolean isActivo() {
    return activo;
  }

  public void setActivo(boolean activo) {
    this.activo = activo;
  }

  public Contenedor getContenedor() {
    return contenedor;
  }

  public void setContenedor(Contenedor contenedor) {
    this.contenedor = contenedor;
  }
}
