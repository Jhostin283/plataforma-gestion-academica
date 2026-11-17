package pe.academica.cursos.domain;

import java.util.List;

public record PaginaCursos(List<Curso> items, int page, int size, long total) {
    public PaginaCursos { items = List.copyOf(items); }
}
