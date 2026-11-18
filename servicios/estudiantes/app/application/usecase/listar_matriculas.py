from uuid import UUID

from app.application.port.out.matricula_repository import MatriculaRepository


class ListarMatriculas:
    def __init__(self, repositorio: MatriculaRepository):
        self.repositorio = repositorio

    def ejecutar(self, estudiante_id: UUID | None, curso_id: UUID | None, estado: str | None, page: int, size: int):
        return self.repositorio.listar(estudiante_id, curso_id, estado, page, size)
