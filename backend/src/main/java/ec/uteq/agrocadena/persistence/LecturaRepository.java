// src/main/java/ec/uteq/agrocadena/persistence/LecturaRepository.java
package ec.uteq.agrocadena.persistence;

import ec.uteq.agrocadena.domain.Lectura;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface LecturaRepository extends JpaRepository<Lectura, Long> {
	
	@Query("""
	SELECT l FROM Lectura l
	WHERE l.dispositivo.contenedor.codigo = :codigo
	ORDER BY l.instante DESC
	""")
	List<Lectura> ultimasPorContenedor(@Param("codigo") String codigo,
	org.springframework.data.domain.Pageable p);
	
	/** Lecturas de un contenedor desde un instante (usado por KpiService, cap. 3). */
	@Query("""
	SELECT l FROM Lectura l
	WHERE l.dispositivo.contenedor.codigo = :codigo
	AND l.instante >= :desde
	ORDER BY l.instante
	""")
	List<Lectura> lecturasPorContenedor(@Param("codigo") String codigo,
	@Param("desde") java.time.Instant desde);
}
