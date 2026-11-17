from uuid import uuid4
from app.application.port.out.estudiante_repository import EstudianteRepository
from app.domain.entity.estudiante import Estudiante


class RegistrarEstudiante:
    def __init__(self, repository: EstudianteRepository):
        self.repository = repository

    def ejecutar(self, documento: str, nombres: str, apellidos: str, correo: str) -> Estudiante:
        estudiante = Estudiante(uuid4(), documento.strip(), nombres.strip(), apellidos.strip(), correo.strip())
        return self.repository.guardar(estudiante)
