from uuid import uuid4
from dataclasses import replace
from fastapi.testclient import TestClient
from app.adapters.in_web.routes import repository
from app.application.exception.errores import DocumentoDuplicado, EstudianteNoEncontrado
from app.application.usecase.registrar_estudiante import RegistrarEstudiante
from app.main import app


class RepositorioSimulado:
    def __init__(self):
        self.estudiantes = {}

    def guardar(self, estudiante):
        if any(x.documento == estudiante.documento for x in self.estudiantes.values()):
            raise DocumentoDuplicado()
        self.estudiantes[estudiante.id] = estudiante
        return estudiante

    def buscar_por_id(self, estudiante_id):
        return self.estudiantes.get(estudiante_id)

    def actualizar(self, estudiante):
        if any(x.documento == estudiante.documento and x.id != estudiante.id for x in self.estudiantes.values()):
            raise DocumentoDuplicado()
        self.estudiantes[estudiante.id] = estudiante
        return estudiante

    def cambiar_estado(self, estudiante_id, estado):
        estudiante = self.estudiantes.get(estudiante_id)
        if estudiante is None:
            raise EstudianteNoEncontrado()
        estudiante = replace(estudiante, estado=estado)
        self.estudiantes[estudiante_id] = estudiante
        return estudiante

    def listar(self, buscar, page, size):
        texto = (buscar or "").strip().casefold()
        resultados = sorted((x for x in self.estudiantes.values() if any(
            texto in valor.casefold() for valor in (x.nombres, x.apellidos, x.documento))),
            key=lambda x: (x.apellidos, x.nombres, x.id))
        return resultados[page * size:(page + 1) * size], len(resultados)


def cliente():
    repo = RepositorioSimulado()
    app.dependency_overrides[repository] = lambda: repo
    return TestClient(app), repo


def test_caso_de_uso_registra_estudiante_activo():
    repo = RepositorioSimulado()
    estudiante = RegistrarEstudiante(repo).ejecutar(" 123 ", " Ana ", " Pérez ", "ana@example.com")
    assert estudiante.documento == "123"
    assert estudiante.estado == "ACTIVO"
    assert repo.buscar_por_id(estudiante.id) == estudiante


def test_registro_y_consulta():
    client, _ = cliente()
    try:
        response = client.post("/api/v1/estudiantes", json={"documento": "123", "nombres": "Ana", "apellidos": "Pérez", "correo": "ana@example.com"})
        assert response.status_code == 201
        assert response.json()["estado"] == "ACTIVO"
        assert client.get(response.headers["location"]).json()["documento"] == "123"
        assert client.get(f"/api/v1/estudiantes/{uuid4()}").status_code == 404
    finally:
        app.dependency_overrides.clear()


def test_duplicado_y_datos_invalidos():
    client, _ = cliente()
    try:
        datos = {"documento": "123", "nombres": "Ana", "apellidos": "Pérez", "correo": "ana@example.com"}
        assert client.post("/api/v1/estudiantes", json=datos).status_code == 201
        assert client.post("/api/v1/estudiantes", json=datos).status_code == 409
        assert client.post("/api/v1/estudiantes", json={**datos, "correo": "incorrecto"}).status_code == 422
        assert client.post("/api/v1/estudiantes", content="{", headers={"content-type": "application/json"}).status_code == 400
    finally:
        app.dependency_overrides.clear()


def test_est_02_listado_filtros_y_paginacion():
    client, _ = cliente()
    try:
        for documento, nombres, apellidos in [("123", "Ana", "Pérez"), ("456", "Luis", "Pérez"), ("789", "Ana", "Rojas")]:
            client.post("/api/v1/estudiantes", json={"documento": documento, "nombres": nombres,
                        "apellidos": apellidos, "correo": "alumno@example.com"})
        for termino, total in [("ana", 2), ("PÉREZ", 2), ("456", 1), ("inexistente", 0)]:
            response = client.get("/api/v1/estudiantes", params={"buscar": termino})
            assert response.status_code == 200
            assert response.json()["total"] == total
            assert len(response.json()["items"]) == total
        pagina = client.get("/api/v1/estudiantes", params={"page": 1, "size": 1}).json()
        assert pagina["total"] == 3 and len(pagina["items"]) == 1
        assert set(pagina["items"][0]) == {"id", "documento", "nombres", "apellidos", "correo", "estado"}
        for parametros in ({"page": -1}, {"size": 0}, {"size": 101}, {"page": "x"}):
            assert client.get("/api/v1/estudiantes", params=parametros).status_code == 400
        assert client.get("/api/v1/estudiantes/no-es-uuid").status_code == 400
    finally:
        app.dependency_overrides.clear()


def test_est_03_actualizacion_y_rechazos():
    client, _ = cliente()
    try:
        original = {"documento": "123", "nombres": "Ana", "apellidos": "Pérez", "correo": "ana@example.com"}
        otro = {**original, "documento": "456"}
        ubicacion = client.post("/api/v1/estudiantes", json=original).headers["location"]
        client.post("/api/v1/estudiantes", json=otro)
        nuevos = {**original, "nombres": "María"}
        actualizado = client.put(ubicacion, json=nuevos)
        assert actualizado.status_code == 200
        assert actualizado.json()["nombres"] == "María"
        assert actualizado.json()["estado"] == "ACTIVO"
        assert client.put(ubicacion, json={**nuevos, "documento": "456"}).status_code == 409
        assert client.put(ubicacion, json={**nuevos, "correo": "inválido"}).status_code == 422
        assert client.put(ubicacion, json={"documento": "123"}).status_code == 422
        assert client.get(ubicacion).json()["nombres"] == "María"
        assert client.put(f"/api/v1/estudiantes/{uuid4()}", json=nuevos).status_code == 404
        assert client.put("/api/v1/estudiantes/invalido", json=nuevos).status_code == 400
    finally:
        app.dependency_overrides.clear()


def test_est_04_cambiar_estado_y_rechazos():
    client, _ = cliente()
    try:
        datos = {"documento": "123", "nombres": "Ana", "apellidos": "Pérez", "correo": "ana@example.com"}
        ubicacion = client.post("/api/v1/estudiantes", json=datos).headers["location"]
        for estado in ("INACTIVO", "INACTIVO", "ACTIVO"):
            respuesta = client.patch(f"{ubicacion}/estado", json={"estado": estado})
            assert respuesta.status_code == 200
            assert respuesta.json()["estado"] == estado
            assert respuesta.json()["documento"] == "123"
            assert client.get(ubicacion).json()["estado"] == estado
        assert client.patch(f"{ubicacion}/estado", json={"estado": "OTRO"}).status_code == 422
        assert client.patch(f"/api/v1/estudiantes/{uuid4()}/estado", json={"estado": "ACTIVO"}).status_code == 404
        assert client.patch("/api/v1/estudiantes/invalido/estado", json={"estado": "ACTIVO"}).status_code == 400
    finally:
        app.dependency_overrides.clear()
