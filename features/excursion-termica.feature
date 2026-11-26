# language: es
# features/excursion-termica.feature
Característica: Deteccion de excursion termica sostenida
  Como jefe de logistica
  quiero recibir una alerta critica
  cuando la temperatura quede fuera del rango por diez minutos
  para reaccionar antes de que el producto pierda calidad

  @RF-03
  Escenario: Excursion sostenida sobre el limite superior
    Dado un contenedor "CNT-007" con producto banano y rango [-1, 8] degC
    Cuando recibo lecturas con 9.5 degC durante 12 minutos consecutivos
    Entonces se debe registrar una alerta de tipo "excursion_termica"
    Y la app movil del jefe de logistica recibe una notificacion push
    Y el tablero web muestra el contenedor en estado rojo

  @RF-03
  Escenario: Excursion breve no genera alerta
    Dado un contenedor "CNT-007" con producto banano y rango [-1, 8] degC
    Cuando recibo lecturas con 8.5 degC durante 6 minutos y luego vuelve a rango
    Entonces no se debe registrar alerta critica
    Y el tablero web muestra el contenedor en estado ambar
