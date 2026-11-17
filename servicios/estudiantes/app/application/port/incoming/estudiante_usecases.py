from typing import Protocol
from uuid import UUID
from app.domain.entity.estudiante import Estudiante


class RegistrarEstudianteUseCase(Protocol):
    def ejecutar(self, documento: str, nombres: str, apellidos: str, correo: str) -> Estudiante: ...


class ConsultarEstudianteUseCase(Protocol):
    def ejecutar(self, estudiante_id: UUID) -> Estudiante: ...
