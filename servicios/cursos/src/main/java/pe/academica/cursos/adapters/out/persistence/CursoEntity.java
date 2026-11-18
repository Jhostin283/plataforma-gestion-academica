package pe.academica.cursos.adapters.out.persistence;

import jakarta.persistence.*;
import java.util.UUID;
import pe.academica.cursos.domain.Curso;

@Entity
@Table(name = "cursos", uniqueConstraints = @UniqueConstraint(name = "uq_cursos_codigo", columnNames = "codigo"))
class CursoEntity {
    @Id UUID id;
    @Column(nullable = false, length = 100) String codigo;
    @Column(nullable = false) String nombre;
    @Column(nullable = false, columnDefinition = "TEXT") String descripcion;
    @Column(name = "duracion_horas", nullable = false) int duracionHoras;
    @Column(nullable = false) int capacidad;
    @Column(nullable = false, length = 20) String estado;

    protected CursoEntity() { }
    CursoEntity(Curso curso) {
        id = curso.id(); codigo = curso.codigo(); nombre = curso.nombre(); descripcion = curso.descripcion();
        duracionHoras = curso.duracionHoras(); capacidad = curso.capacidad(); estado = curso.estado();
    }
    Curso aDominio() { return new Curso(id, codigo, nombre, descripcion, duracionHoras, capacidad, estado); }
}
