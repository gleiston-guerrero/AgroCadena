// backend/src/main/java/ec/uteq/agrocadena/persistence/DispositivoRepository.java
package ec.uteq.agrocadena.persistence;

import ec.uteq.agrocadena.domain.Dispositivo;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DispositivoRepository
  extends JpaRepository<Dispositivo, Long>
{
  Optional<Dispositivo> findByDevEui(String devEui);
}
