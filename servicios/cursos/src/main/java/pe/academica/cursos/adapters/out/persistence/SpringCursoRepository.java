package pe.academica.cursos.adapters.out.persistence;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Lock;
import jakarta.persistence.LockModeType;
import java.util.Optional;

interface SpringCursoRepository extends JpaRepository<CursoEntity, UUID> {
    @Query("select c from CursoEntity c where lower(c.nombre) like lower(concat('%', :buscar, '%')) " +
           "or lower(c.codigo) like lower(concat('%', :buscar, '%'))")
    Page<CursoEntity> buscar(@Param("buscar") String buscar, Pageable pageable);
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select c from CursoEntity c where c.id = :id")
    Optional<CursoEntity> buscarParaActualizar(@Param("id") UUID id);
}
