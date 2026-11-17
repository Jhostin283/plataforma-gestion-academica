from typing import Protocol
from uuid import UUID

from app.domain.entity.matricula import Matricula


class MatriculaRepository(Protocol):
    def iniciar(self, operacion_id: UUID, estudiante_id: UUID, curso_id: UUID) -> Matricula: ...
    def obtener(self, matricula_id: UUID) -> Matricula | None: ...
    def cambiar_estado(self, matricula_id: UUID, estado: str, codigo_rechazo: int | None = None,
                       estado_actual: str = 'PENDIENTE') -> Matricula: ...
