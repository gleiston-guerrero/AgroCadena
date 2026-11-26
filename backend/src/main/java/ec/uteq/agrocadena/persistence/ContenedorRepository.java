// backend/src/main/java/ec/uteq/agrocadena/persistence/ContenedorRepository.java
package ec.uteq.agrocadena.persistence;

import ec.uteq.agrocadena.domain.Contenedor;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContenedorRepository
  extends JpaRepository<Contenedor, Long>
{
  Optional<Contenedor> findByCodigo(String codigo);
}
