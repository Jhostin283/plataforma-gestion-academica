package pe.academica.cursos.application.model;

import pe.academica.cursos.domain.entity.Curso;
import java.util.List;

public record PaginaCursos(List<Curso> items, int page, int size, long total) {
    public PaginaCursos { items = List.copyOf(items); }
}
