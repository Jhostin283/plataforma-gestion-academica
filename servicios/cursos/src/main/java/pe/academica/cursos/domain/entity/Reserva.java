package pe.academica.cursos.domain.entity;

import java.util.UUID;

public record Reserva(UUID operacionId, UUID cursoId, String estado) { }
