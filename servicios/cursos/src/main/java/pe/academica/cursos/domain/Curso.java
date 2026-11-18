package pe.academica.cursos.domain;

import java.util.UUID;

public record Curso(UUID id, String codigo, String nombre, String descripcion,
                    int duracionHoras, int capacidad, String estado) { }
