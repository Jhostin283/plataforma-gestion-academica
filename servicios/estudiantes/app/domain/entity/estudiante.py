from dataclasses import dataclass
from uuid import UUID


@dataclass(frozen=True, slots=True)
class Estudiante:
    id: UUID
    documento: str
    nombres: str
    apellidos: str
    correo: str
    estado: str = "ACTIVO"
