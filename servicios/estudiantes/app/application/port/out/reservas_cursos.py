from dataclasses import dataclass
from typing import Literal, Protocol
from uuid import UUID


@dataclass(frozen=True, slots=True)
class Reserva:
    operacion_id: UUID
    curso_id: UUID
    estado: Literal['RESERVADA', 'LIBERADA']


class ReservaRechazada(Exception):
    def __init__(self, status: int):
        self.status = status
        super().__init__('La reserva fue rechazada')


class ResultadoReservaIncierto(Exception):
    pass


class ReservasCursos(Protocol):
    def reservar(self, curso_id: UUID, operacion_id: UUID) -> Reserva: ...
    def consultar(self, operacion_id: UUID) -> Reserva: ...
    def liberar(self, operacion_id: UUID) -> Reserva: ...
