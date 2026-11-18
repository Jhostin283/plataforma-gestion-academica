from typing import Protocol
from uuid import UUID
from app.domain.entity.estudiante import Estudiante


class EstudianteRepository(Protocol):
    def guardar(self, estudiante: Estudiante) -> Estudiante: ...
    def buscar_por_id(self, estudiante_id: UUID) -> Estudiante | None: ...
