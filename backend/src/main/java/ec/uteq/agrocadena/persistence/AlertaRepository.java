// backend/src/main/java/ec/uteq/agrocadena/persistence/AlertaRepository.java
package ec.uteq.agrocadena.persistence;

import ec.uteq.agrocadena.domain.Alerta;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AlertaRepository extends JpaRepository<Alerta, Long> {
  List<Alerta> findByIdGreaterThanOrderByIdAsc(long id);

  @Query("SELECT COALESCE(MAX(a.id), 0) FROM Alerta a")
  long maximoId();
}
