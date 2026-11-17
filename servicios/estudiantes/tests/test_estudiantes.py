from uuid import uuid4
from fastapi.testclient import TestClient
from app.adapters.in_web.routes import repository
from app.application.exception.errores import DocumentoDuplicado
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
