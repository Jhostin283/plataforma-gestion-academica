package pe.academica.cursos.adapters.out.persistence;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

interface SpringCursoRepository extends JpaRepository<CursoEntity, UUID> { }
