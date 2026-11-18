CREATE TABLE reservas (
    operacion_id UUID PRIMARY KEY,
    curso_id UUID NOT NULL REFERENCES cursos(id),
    estado VARCHAR(20) NOT NULL CHECK (estado IN ('RESERVADA', 'LIBERADA'))
);
CREATE INDEX ix_reservas_curso_estado ON reservas (curso_id, estado);
