package ec.uteq.agrocadena.persistence;

import ec.uteq.agrocadena.domain.Dispositivo;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface DispositivoRepository extends JpaRepository<Dispositivo, Long> {
	Optional<Dispositivo> findByDevEui(String devEui);
}
