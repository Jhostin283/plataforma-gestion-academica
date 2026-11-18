from uuid import UUID
from app.application.exception.errores import EstudianteNoEncontrado
from app.application.port.out.estudiante_repository import EstudianteRepository
from app.domain.entity.estudiante import Estudiante


class ConsultarEstudiante:
    def __init__(self, repository: EstudianteRepository):
        self.repository = repository

    def ejecutar(self, estudiante_id: UUID) -> Estudiante:
        estudiante = self.repository.buscar_por_id(estudiante_id)
        if estudiante is None:
            raise EstudianteNoEncontrado()
        return estudiante
