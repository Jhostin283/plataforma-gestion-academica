from dataclasses import dataclass
from uuid import UUID


@dataclass(frozen=True, slots=True)
class Matricula:
    id: UUID
    operacion_id: UUID
    estudiante_id: UUID
    curso_id: UUID
    estado: str
    codigo_rechazo: int | None = None
