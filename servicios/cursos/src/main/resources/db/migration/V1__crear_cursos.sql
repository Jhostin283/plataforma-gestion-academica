CREATE TABLE cursos (
    id UUID PRIMARY KEY,
    codigo VARCHAR(100) NOT NULL,
    nombre VARCHAR(255) NOT NULL,
    descripcion TEXT NOT NULL,
    duracion_horas INTEGER NOT NULL CHECK (duracion_horas > 0),
    capacidad INTEGER NOT NULL CHECK (capacidad > 0),
    estado VARCHAR(20) NOT NULL CHECK (estado IN ('ACTIVO', 'INACTIVO')),
    CONSTRAINT uq_cursos_codigo UNIQUE (codigo)
);
