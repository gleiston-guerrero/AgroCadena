package ec.uteq.agrocadena.persistence;

import ec.uteq.agrocadena.domain.Contenedor;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ContenedorRepository extends JpaRepository<Contenedor, Long> {
	Optional<Contenedor> findByCodigo(String codigo);
}
