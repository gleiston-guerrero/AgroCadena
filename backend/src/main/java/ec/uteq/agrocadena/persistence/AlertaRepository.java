package ec.uteq.agrocadena.persistence;

import ec.uteq.agrocadena.domain.Alerta;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlertaRepository extends JpaRepository<Alerta, Long> { }
