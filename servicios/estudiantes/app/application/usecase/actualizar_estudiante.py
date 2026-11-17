from dataclasses import replace
from uuid import UUID

from app.application.exception.errores import EstudianteNoEncontrado
from app.application.port.out.estudiante_repository import EstudianteRepository


class ActualizarEstudiante:
    def __init__(self, repositorio: EstudianteRepository):
        self.repositorio = repositorio

    def ejecutar(self, estudiante_id: UUID, documento: str, nombres: str, apellidos: str, correo: str):
        estudiante = self.repositorio.buscar_por_id(estudiante_id)
        if estudiante is None:
            raise EstudianteNoEncontrado()
        return self.repositorio.actualizar(replace(estudiante, documento=documento, nombres=nombres,
                                                   apellidos=apellidos, correo=correo))
