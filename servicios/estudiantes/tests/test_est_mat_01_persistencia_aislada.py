from types import SimpleNamespace
from unittest.mock import Mock
from uuid import uuid4

import pytest
from sqlalchemy.exc import IntegrityError, SQLAlchemyError

from app.adapters.out.persistence.matricula_repository import SqlAlchemyMatriculaRepository
from app.adapters.out.persistence.modelos import MatriculaModel
from app.application.exception.errores import ConflictoMatricula, EstudianteNoEncontrado, ResultadoPersistenciaIncierto


def modelo(operacion, estudiante, curso, estado='PENDIENTE'):
    return MatriculaModel(id=uuid4(), operacion_id=operacion, estudiante_id=estudiante,
                          curso_id=curso, estado=estado, codigo_rechazo=None)


def conflicto(nombre):
    original = Exception('error de integridad')
    original.diag = SimpleNamespace(constraint_name=nombre)
    return IntegrityError('INSERT', {}, original)


def test_iniciar_guarda_operacion_pendiente_antes_de_reservar():
    session = Mock()
    session.scalar.side_effect = [None, SimpleNamespace(estado='ACTIVO')]
    operacion, estudiante, curso = uuid4(), uuid4(), uuid4()
    resultado = SqlAlchemyMatriculaRepository(session).iniciar(operacion, estudiante, curso)
    assert (resultado.operacion_id, resultado.estudiante_id, resultado.curso_id, resultado.estado) == (
        operacion, estudiante, curso, 'PENDIENTE')
    session.add.assert_called_once()
    session.commit.assert_called_once()


def test_repeticion_con_mismos_datos_y_rechazo_de_clave_reutilizada():
    session = Mock()
    operacion, estudiante, curso = uuid4(), uuid4(), uuid4()
    registro = modelo(operacion, estudiante, curso)
    session.scalar.return_value = registro
    repo = SqlAlchemyMatriculaRepository(session)
    assert repo.iniciar(operacion, estudiante, curso).id == registro.id
    session.commit.assert_not_called()
    with pytest.raises(ConflictoMatricula):
        repo.iniciar(operacion, estudiante, uuid4())


@pytest.mark.parametrize('estado, error', [(None, EstudianteNoEncontrado), ('INACTIVO', ConflictoMatricula)])
def test_iniciar_rechaza_estudiante_inexistente_o_inactivo(estado, error):
    session = Mock()
    session.scalar.side_effect = [None, None if estado is None else SimpleNamespace(estado=estado)]
    with pytest.raises(error):
        SqlAlchemyMatriculaRepository(session).iniciar(uuid4(), uuid4(), uuid4())
    session.rollback.assert_called_once()
    session.commit.assert_not_called()


def test_colision_de_clave_recupera_operacion_existente():
    session = Mock()
    operacion, estudiante, curso = uuid4(), uuid4(), uuid4()
    existente = modelo(operacion, estudiante, curso)
    session.scalar.side_effect = [None, SimpleNamespace(estado='ACTIVO'), existente]
    session.commit.side_effect = conflicto('uq_matriculas_operacion')
    resultado = SqlAlchemyMatriculaRepository(session).iniciar(operacion, estudiante, curso)
    assert resultado.id == existente.id
    assert session.rollback.call_count == 2


def test_restriccion_de_matricula_vigente_se_traduce_a_conflicto():
    session = Mock()
    session.scalar.side_effect = [None, SimpleNamespace(estado='ACTIVO'), None]
    session.commit.side_effect = conflicto('uq_matriculas_estudiante_curso_vigente')
    with pytest.raises(ConflictoMatricula):
        SqlAlchemyMatriculaRepository(session).iniciar(uuid4(), uuid4(), uuid4())
    assert session.rollback.call_count == 2


def test_otro_error_de_integridad_no_se_oculta():
    session = Mock()
    session.scalar.side_effect = [None, SimpleNamespace(estado='ACTIVO'), None]
    session.commit.side_effect = conflicto('otra_restriccion')
    with pytest.raises(IntegrityError):
        SqlAlchemyMatriculaRepository(session).iniciar(uuid4(), uuid4(), uuid4())


def test_consulta_listado_y_pendientes_con_sesion_aislada():
    session = Mock()
    registro = modelo(uuid4(), uuid4(), uuid4())
    repo = SqlAlchemyMatriculaRepository(session)
    session.get.return_value = registro
    assert repo.obtener(registro.id).id == registro.id
    session.get.return_value = None
    assert repo.obtener(uuid4()) is None
    session.scalar.return_value = 1
    session.scalars.return_value.all.return_value = [registro]
    items, total = repo.listar(registro.estudiante_id, registro.curso_id, 'PENDIENTE', 0, 20)
    assert total == 1 and items[0].id == registro.id
    assert repo.pendientes(despues_de=uuid4())[0].id == registro.id
    assert session.rollback.call_count == 4


def test_transicion_y_fallo_incierto_de_persistencia():
    session = Mock()
    registro = modelo(uuid4(), uuid4(), uuid4(), 'ACTIVA')
    session.execute.return_value.scalar_one_or_none.return_value = registro
    repo = SqlAlchemyMatriculaRepository(session)
    assert repo.cambiar_estado(registro.id, 'ACTIVA').estado == 'ACTIVA'
    session.commit.assert_called_once()
    session.execute.side_effect = SQLAlchemyError('fallo de almacenamiento')
    with pytest.raises(ResultadoPersistenciaIncierto):
        repo.cambiar_estado(registro.id, 'ACTIVA')
    session.rollback.assert_called_once()


def test_transicion_concurrente_devuelve_estado_existente():
    session = Mock()
    registro = modelo(uuid4(), uuid4(), uuid4(), 'ACTIVA')
    session.execute.return_value.scalar_one_or_none.return_value = None
    session.get.return_value = registro
    assert SqlAlchemyMatriculaRepository(session).cambiar_estado(registro.id, 'FALLIDA').estado == 'ACTIVA'
