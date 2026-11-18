from types import SimpleNamespace
from unittest.mock import Mock
from uuid import uuid4

import pytest
from sqlalchemy.exc import IntegrityError

from app.adapters.out.persistence.estudiante_repository import SqlAlchemyEstudianteRepository
from app.application.exception.errores import DocumentoDuplicado
from app.domain.entity.estudiante import Estudiante


def estudiante():
    return Estudiante(uuid4(), '123', 'Ana', 'Pérez', 'ana@example.com', 'ACTIVO')


def conflicto(nombre):
    original = Exception('error de integridad')
    original.diag = SimpleNamespace(constraint_name=nombre)
    return IntegrityError('INSERT', {}, original)


def test_guardar_y_consultar_solicita_commit_y_convierte_datos():
    session = Mock()
    repo = SqlAlchemyEstudianteRepository(session)
    valor = estudiante()
    assert repo.guardar(valor) == valor
    modelo = session.add.call_args.args[0]
    assert modelo.documento == valor.documento
    assert modelo.estado == 'ACTIVO'
    session.commit.assert_called_once()
    session.get.return_value = modelo
    assert repo.buscar_por_id(valor.id) == valor
    session.get.return_value = None
    assert repo.buscar_por_id(valor.id) is None


def test_documento_duplicado_revierte_y_traduce_conflicto():
    session = Mock()
    session.commit.side_effect = conflicto('uq_estudiantes_documento')
    with pytest.raises(DocumentoDuplicado):
        SqlAlchemyEstudianteRepository(session).guardar(estudiante())
    session.rollback.assert_called_once()


def test_otras_violaciones_no_se_presentan_como_duplicados():
    session = Mock()
    session.commit.side_effect = conflicto('otra_restriccion')
    with pytest.raises(IntegrityError):
        SqlAlchemyEstudianteRepository(session).guardar(estudiante())
    session.rollback.assert_called_once()
