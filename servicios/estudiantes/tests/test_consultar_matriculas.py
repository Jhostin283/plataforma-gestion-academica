from uuid import uuid4

from fastapi.testclient import TestClient

from app.adapters.in_web.matriculas_routes import repositorio
from app.domain.entity.matricula import Matricula
from app.main import app


def test_filtros_paginacion_detalle_y_errores():
    estudiante, curso, clave = uuid4(), uuid4(), uuid4()
    registro = Matricula(uuid4(), clave, estudiante, curso, 'PENDIENTE')

    class Repo:
        def listar(self, estudiante_id, curso_id, estado, page, size):
            coincide = (estudiante_id in (None, estudiante) and curso_id in (None, curso)
                        and estado in (None, 'PENDIENTE'))
            return ([registro] if coincide and page == 0 else []), int(coincide)

        def obtener(self, identificador):
            return registro if identificador == registro.id else None

    app.dependency_overrides[repositorio] = lambda: Repo()
    try:
        client = TestClient(app)
        ruta = '/api/v1/matriculas'
        filtrado = client.get(ruta, params={'estudianteId': str(estudiante), 'cursoId': str(curso), 'estado': 'PENDIENTE'})
        assert filtrado.status_code == 200 and filtrado.json()['total'] == 1
        assert filtrado.json()['items'][0]['estado'] == 'PENDIENTE'
        assert client.get(ruta, params={'cursoId': str(uuid4())}).json()['items'] == []
        assert client.get(ruta, params={'page': 1, 'size': 1}).json()['items'] == []
        assert client.get(f'{ruta}/{registro.id}').status_code == 200
        assert client.get(f'{ruta}/{uuid4()}').status_code == 404
        assert client.get(f'{ruta}/invalido').status_code == 400
        for params in ({'page': -1}, {'size': 0}, {'size': 101}, {'estudianteId': 'invalido'}, {'estado': 'OTRO'}):
            assert client.get(ruta, params=params).status_code == 400
    finally:
        app.dependency_overrides.clear()
