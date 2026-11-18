from app.application.port.out.estudiante_repository import EstudianteRepository


class ListarEstudiantes:
    def __init__(self, repositorio: EstudianteRepository):
        self.repositorio = repositorio

    def ejecutar(self, buscar: str | None, page: int, size: int):
        return self.repositorio.listar(buscar, page, size)
