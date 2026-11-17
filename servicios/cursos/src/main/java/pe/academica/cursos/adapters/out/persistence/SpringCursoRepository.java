package pe.academica.cursos.adapters.out.persistence;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

interface SpringCursoRepository extends JpaRepository<CursoEntity, UUID> {
    @Query("select c from CursoEntity c where lower(c.nombre) like lower(concat('%', :buscar, '%')) " +
           "or lower(c.codigo) like lower(concat('%', :buscar, '%'))")
    Page<CursoEntity> buscar(@Param("buscar") String buscar, Pageable pageable);
}
