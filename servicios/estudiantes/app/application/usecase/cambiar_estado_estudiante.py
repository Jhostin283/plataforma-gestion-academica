from uuid import UUID

from app.application.port.out.estudiante_repository import EstudianteRepository


class CambiarEstadoEstudiante:
    def __init__(self, repositorio: EstudianteRepository):
        self.repositorio = repositorio

    def ejecutar(self, estudiante_id: UUID, estado: str):
        return self.repositorio.cambiar_estado(estudiante_id, estado)
